import { Component, OnInit, inject, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RecetaService } from '../../services/receta.service';
import { Medicamento, RecetaRequest } from '../../models/models';
import { positivoValidator } from '../../validators/positivo.validator';

@Component({
  selector: 'app-receta-form',
  imports: [ReactiveFormsModule],
  templateUrl: './receta-form.component.html',
  styleUrl: './receta-form.component.css',
})
export class RecetaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(RecetaService);
  private router = inject(Router);

  medicamentos = signal<Medicamento[]>([]);
  saving = signal(false);
  errorMsg = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    pacienteNombre: ['', [Validators.required, Validators.minLength(5)]],
    detalles: this.fb.array([this.crearDetalle()]),
  });

  get pacienteNombre() { return this.form.controls.pacienteNombre; }
  get detalles(): FormArray { return this.form.controls.detalles; }

  ngOnInit(): void {
    this.service.listarMedicamentos().subscribe({
      next: (m) => this.medicamentos.set(m),
      error: () => this.errorMsg.set('No se pudo cargar el catálogo de medicamentos'),
    });
  }

  private crearDetalle() {
    return this.fb.nonNullable.group({
      medicamentoId: [0, [Validators.required, Validators.min(1)]],
      cantidad: [1, [Validators.required, positivoValidator]],
      dosisIndicada: ['', Validators.required],
    });
  }

  agregarDetalle(): void {
    this.detalles.push(this.crearDetalle());
  }

  quitarDetalle(i: number): void {
    if (this.detalles.length > 1) {
      this.detalles.removeAt(i);
    }
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.errorMsg.set(null);

    const raw = this.form.getRawValue();
    const payload: RecetaRequest = {
      pacienteNombre: raw.pacienteNombre,
      detalles: raw.detalles.map((d: any) => ({
        medicamentoId: Number(d.medicamentoId),
        cantidad: Number(d.cantidad),
        dosisIndicada: d.dosisIndicada,
      })),
    };

    this.service.crear(payload).subscribe({
      next: () => this.router.navigate(['/recetas']),
      error: (err) => {
        this.saving.set(false);
        this.errorMsg.set(err.error?.detail ?? 'No se pudo crear la receta');
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/recetas']);
  }
}
