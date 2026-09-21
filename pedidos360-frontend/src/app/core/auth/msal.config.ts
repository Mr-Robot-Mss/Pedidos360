import {
  BrowserCacheLocation,
  InteractionType,
  IPublicClientApplication,
  LogLevel,
  PublicClientApplication
} from '@azure/msal-browser';
import { MsalGuardConfiguration, MsalInterceptorConfiguration } from '@azure/msal-angular';
import { environment } from '../../../environments/environment';

export const apiScope = `api://${environment.apiClientId}/Pedidos.ReadWrite`;

export function msalInstanceFactory(): IPublicClientApplication {
  return new PublicClientApplication({
    auth: {
      clientId: environment.clientId,
      authority: environment.authority,
      redirectUri: window.location.origin,
      postLogoutRedirectUri: window.location.origin
    },
    cache: { cacheLocation: BrowserCacheLocation.SessionStorage },
    system: {
      loggerOptions: {
        logLevel: LogLevel.Warning,
        piiLoggingEnabled: false,
        loggerCallback: (level, message) => level <= LogLevel.Warning && console.warn(message)
      }
    }
  });
}

export function msalGuardConfigFactory(): MsalGuardConfiguration {
  return {
    interactionType: InteractionType.Redirect,
    authRequest: { scopes: [apiScope] },
    loginFailedRoute: '/sin-autorizacion'
  };
}

export function msalInterceptorConfigFactory(): MsalInterceptorConfiguration {
  return {
    interactionType: InteractionType.Redirect,
    protectedResourceMap: new Map([
      [`${environment.apiUrl}/*`, [apiScope]],
      [environment.apiUrl, [apiScope]]
    ])
  };
}
