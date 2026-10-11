import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// Valida que el valor sea un número entero estrictamente mayor a 0
export const positivoValidator: ValidatorFn = (
  control: AbstractControl
): ValidationErrors | null => {
  const value = control.value;
  if (value === null || value === undefined || value === '') {
    return null; // "required" se encarga de los vacíos
  }
  const n = Number(value);
  return Number.isInteger(n) && n > 0 ? null : { positivo: true };
};
