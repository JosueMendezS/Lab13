export interface LoginRequest {
    username: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    username: string;
    rol: string;
}

export interface Medicamento {
    id: number;
    codigo: string;
    nombre: string;
    stock: number;
    precioUnitario: number;
}

export interface DetalleReceta {
    medicamentoId: number;
    medicamentoNombre: string;
    cantidad: number;
    dosisIndicada: string;
}

export interface Receta {
    id: number;
    codigoReceta: string;
    pacienteNombre: string;
    medicoNombre: string;
    estado: 'PENDIENTE' | 'DESPACHADA' | 'CANCELADA';
    fechaEmision: string;
    detalles: DetalleReceta[];
}

export interface RecetaRequest {
    pacienteNombre: string;
    detalles: {
        medicamentoId: number;
        cantidad: number;
        dosisIndicada: string;
    }[];
}