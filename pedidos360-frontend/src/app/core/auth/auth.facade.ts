import { Injectable, computed, signal } from '@angular/core';
import { MsalBroadcastService, MsalService } from '@azure/msal-angular';
import {
  AccountInfo,
  AuthenticationResult,
  EventMessage,
  EventType
} from '@azure/msal-browser';
import {
  ReplaySubject,
  catchError,
  filter,
  map,
  of,
  switchMap
} from 'rxjs';
import { apiScope } from './msal.config';

type TokenClaims = {
  roles?: string[];
  scp?: string;
  name?: string;
  preferred_username?: string;
};

@Injectable({ providedIn: 'root' })
export class AuthFacade {
  private readonly accountSignal = signal<AccountInfo | null>(null);
  private readonly accessClaimsSignal = signal<TokenClaims>({});
  private readonly readySubject = new ReplaySubject<void>(1);

  readonly ready$ = this.readySubject.asObservable();
  readonly account = this.accountSignal.asReadonly();
  readonly authenticated = computed(() => this.accountSignal() !== null);

  readonly claims = computed(
    () => (this.accountSignal()?.idTokenClaims ?? {}) as TokenClaims
  );

  readonly roles = computed(
    () => this.accessClaimsSignal().roles ?? this.claims().roles ?? []
  );

  readonly scopes = computed(() =>
    (this.accessClaimsSignal().scp ?? '').split(' ').filter(Boolean)
  );

  readonly displayName = computed(
    () => this.claims().name ?? this.accountSignal()?.name ?? 'Usuario'
  );

  constructor(
    private readonly msal: MsalService,
    broadcast: MsalBroadcastService
  ) {
    this.msal.initialize()
      .pipe(
        switchMap(() => this.msal.handleRedirectObservable()),
        switchMap(result => {
          const account =
            result?.account ??
            this.msal.instance.getActiveAccount() ??
            this.msal.instance.getAllAccounts()[0] ??
            null;

          return this.loadAccount(account);
        }),
        catchError(error => {
          console.error(
            'No fue posible inicializar la autenticación.',
            error
          );

          return of(void 0);
        })
      )
      .subscribe(() => this.readySubject.next());

    broadcast.msalSubject$
      .pipe(
        filter(
          (event: EventMessage) =>
            event.eventType === EventType.LOGIN_SUCCESS
        )
      )
      .subscribe(event => {
        const account = (event.payload as AuthenticationResult).account;

        this.loadAccount(account).subscribe(() =>
          this.readySubject.next()
        );
      });
  }

  login(): void {
    this.msal.initialize().subscribe(() =>
      this.msal.loginRedirect({
        scopes: [apiScope]
      })
    );
  }

  logout(): void {
    this.msal.logoutRedirect({
      postLogoutRedirectUri: window.location.origin
    });
  }

  hasAnyRole(required: string[]): boolean {
    return required.some(role => this.roles().includes(role));
  }

  private loadAccount(account: AccountInfo | null) {
    if (!account) {
      this.accountSignal.set(null);
      this.accessClaimsSignal.set({});

      return of(void 0);
    }

    this.msal.instance.setActiveAccount(account);
    this.accountSignal.set(account);

    return this.msal.acquireTokenSilent({
      account,
      scopes: [apiScope]
    }).pipe(
      map(result => {
        const claims = this.decodeClaims(result.accessToken);
        this.accessClaimsSignal.set(claims);
      }),
      catchError(error => {
        console.error(
          'No fue posible obtener el token de la API.',
          error
        );

        this.accessClaimsSignal.set({});
        return of(void 0);
      })
    );
  }

  private decodeClaims(token: string): TokenClaims {
    try {
      const encoded = token
        .split('.')[1]
        .replace(/-/g, '+')
        .replace(/_/g, '/');

      return JSON.parse(
        decodeURIComponent(
          atob(encoded)
            .split('')
            .map(
              char =>
                `%${(
                  '00' + char.charCodeAt(0).toString(16)
                ).slice(-2)}`
            )
            .join('')
        )
      ) as TokenClaims;
    } catch {
      return {};
    }
  }
}