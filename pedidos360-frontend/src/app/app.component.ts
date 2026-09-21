import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthFacade } from './core/auth/auth.facade';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <header class="topbar">
      <a class="brand" routerLink="/inicio"><span>P</span> Pedidos360</a>
      <nav>
        @if (auth.authenticated()) {
          <a routerLink="/inicio" routerLinkActive="active">Inicio</a>
          <a routerLink="/pedidos" routerLinkActive="active">Pedidos</a>
          <span class="user">{{ auth.displayName() }}</span>
          <button class="secondary" (click)="auth.logout()">Cerrar sesión</button>
        } @else {
          <button (click)="auth.login()">Iniciar sesión</button>
        }
      </nav>
    </header>
    <main><router-outlet /></main>
  `
})
export class AppComponent {
  constructor(readonly auth: AuthFacade) {}
}
