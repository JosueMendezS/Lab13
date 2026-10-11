import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Medicamento, Receta, RecetaRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class RecetaService {
    private http = inject(HttpClient);
    private readonly API = 'http://localhost:8080/api/v1';

    listar(): Observable<Receta[]> {
        return this.http.get<Receta[]>(`${this.API}/recetas`);
    }

    crear(receta: RecetaRequest): Observable<Receta> {
        return this.http.post<Receta>(`${this.API}/recetas`, receta);
    }

    cambiarEstado(id: number, estado: 'DESPACHADA' | 'CANCELADA'): Observable<Receta> {
        return this.http.patch<Receta>(`${this.API}/recetas/${id}/estado`, { estado });
    }

    listarMedicamentos(): Observable<Medicamento[]> {
        return this.http.get<Medicamento[]>(`${this.API}/medicamentos`);
    }
}