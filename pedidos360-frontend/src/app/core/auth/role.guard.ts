import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, take } from 'rxjs';
import { AuthFacade } from './auth.facade';

export const roleGuard: CanActivateFn = route => {
  const auth = inject(AuthFacade);
  const router = inject(Router);

  const requiredRoles =
    (route.data['roles'] as string[] | undefined) ?? [];

  return auth.ready$.pipe(
    take(1),
    map(() => {
      // Si todavía no hay una cuenta, MsalGuard debe iniciar el login.
      // No corresponde mostrar 403 antes de autenticar al usuario.
      if (!auth.authenticated()) {
        return true;
      }

      return requiredRoles.length === 0 ||
        auth.hasAnyRole(requiredRoles)
        ? true
        : router.createUrlTree(['/sin-autorizacion']);
    })
  );
};