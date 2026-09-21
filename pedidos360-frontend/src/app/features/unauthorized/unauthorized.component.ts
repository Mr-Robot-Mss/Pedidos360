import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({standalone: true, imports: [RouterLink], template: `<div class="state unauthorized"><strong>403</strong><h1>Acceso no autorizado</h1><p>Tu sesión es válida, pero no posee uno de los roles requeridos.</p><a class="button" routerLink="/inicio">Volver al inicio</a></div>`})
export class UnauthorizedComponent {}
