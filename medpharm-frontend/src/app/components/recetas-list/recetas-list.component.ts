import { Component, computed, inject, signal, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { RecetaService } from '../../services/receta.service';
import { AuthService } from '../../services/auth.service';
import { Receta } from '../../models/models';

type Filtro = 'TODAS' | 'PENDIENTE' | 'DESPACHADA';

@Component({
  selector: 'app-recetas-list',
  imports: [DatePipe],
  templateUrl: './recetas-list.component.html',
  styleUrl: './recetas-list.component.css',
})
export class RecetasListComponent implements OnInit {
  private service = inject(RecetaService);
  private router = inject(Router);
  auth = inject(AuthService);

  recetas = signal<Receta[]>([]);
  filtro = signal<Filtro>('TODAS');
  loading = signal(false);
  errorMsg = signal<string | null>(null);

  readonly filtros: Filtro[] = ['TODAS', 'PENDIENTE', 'DESPACHADA'];

  recetasFiltradas = computed(() => {
    const f = this.filtro();
    const lista = this.recetas();
    return f === 'TODAS' ? lista : lista.filter((r) => r.estado === f);
  });

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.loading.set(true);
    this.errorMsg.set(null);
    this.service.listar().subscribe({
      next: (data) => {
        this.recetas.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMsg.set(
          err.status === 401 || err.status === 403
            ? 'No autorizado (' + err.status + '): el servidor rechazó la petición'
            : 'No se pudo cargar las recetas'
        );
      },
    });
  }

  cambiarEstado(r: Receta, estado: 'DESPACHADA' | 'CANCELADA'): void {
    this.service.cambiarEstado(r.id, estado).subscribe({
      next: (actualizada) =>
        this.recetas.update((lista) => lista.map((x) => (x.id === actualizada.id ? actualizada : x))),
      error: (err) =>
        this.errorMsg.set(err.error?.detail ?? 'No se pudo cambiar el estado'),
    });
  }

  nueva(): void {
    this.router.navigate(['/nueva-receta']);
  }

  logout(): void {
    this.auth.logout();
  }
}