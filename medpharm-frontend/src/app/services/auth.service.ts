import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthResponse, LoginRequest } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private http = inject(HttpClient);
    private router = inject(Router);

    private readonly API = 'http://localhost:8080/api/v1/auth';
    private readonly TOKEN_KEY = 'medpharm_token';
    private readonly USER_KEY = 'medpharm_user';

    private tokenSignal = signal<string | null>(localStorage.getItem(this.TOKEN_KEY));
    private userSignal = signal<{ username: string; rol: string } | null>(
        JSON.parse(localStorage.getItem(this.USER_KEY) ?? 'null')
    );

    readonly user = this.userSignal.asReadonly();
    readonly isLoggedIn = computed(() => !!this.tokenSignal());

    login(credentials: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.API}/login`, credentials).pipe(
            tap((res) => {
                localStorage.setItem(this.TOKEN_KEY, res.token);
                localStorage.setItem(
                    this.USER_KEY,
                    JSON.stringify({ username: res.username, rol: res.rol })
                );
                this.tokenSignal.set(res.token);
                this.userSignal.set({ username: res.username, rol: res.rol });
            })
        );
    }

    logout(): void {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
        this.tokenSignal.set(null);
        this.userSignal.set(null);
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        return this.tokenSignal();
    }
}