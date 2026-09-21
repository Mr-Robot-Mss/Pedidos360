import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthFacade } from '../../core/auth/auth.facade';

@Component({
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="hero">
      <div><span class="eyebrow">OPERACIÓN SEGURA</span><h1>Hola, {{ auth.displayName() }}</h1>
      <p>Administra el ciclo completo de tus pedidos desde una vista centralizada y protegida con Microsoft Entra ID.</p>
      <a class="button" routerLink="/pedidos">Revisar pedidos</a></div>
      <div class="security-card"><span class="shield">✓</span><h2>Sesión protegida</h2>
      <p>MSAL administra tu sesión y adjunta el token automáticamente al API.</p></div>
    </section>
    <section class="grid">
      <article><span class="metric">{{ auth.roles().length }}</span><h3>Roles habilitados</h3><p>{{ auth.roles().join(', ') || 'Sin roles declarados' }}</p></article>
      <article><span class="metric">{{ auth.scopes().length }}</span><h3>Scopes del token</h3><p>{{ auth.scopes().join(', ') || 'Los scopes se validan en el access token' }}</p></article>
      <article><span class="metric">360°</span><h3>Trazabilidad</h3><p>Frontend, BFF y microservicios separados.</p></article>
    </section>
  `
})
export class DashboardComponent {
  constructor(readonly auth: AuthFacade) {}
}
