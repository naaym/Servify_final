import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Logo } from '../../../shared/logo/logo';
import { AuthService } from '../services/auth.service';
import { ShowMessageService } from '../../../shared/services/showmessage.service';

@Component({
  selector: 'app-loginclient',
  imports: [ReactiveFormsModule, Logo, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private readonly auth = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly showmessage = inject(ShowMessageService);

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  onSubmit() {
    const payload = this.form.getRawValue();

    this.auth.login(payload).subscribe({
      next: (val) => {
        switch (val.role) {
          case 'ADMIN':
            this.router.navigate(['/admins/dashboard']);
            break;

          case 'PROVIDER':
            if (val.status === 'ACCEPTED') {
              this.router.navigate(['/providers/dashboard']);
              this.showmessage.show('success', val.message);
            }
            if (val.status === 'REJECTED') {
              this.router.navigate(['/providers/rejected']);
              this.showmessage.show('error', val.message);
            }
            if (val.status === 'PENDING') {
              this.router.navigate(['/providers/pending']);
              this.showmessage.show('info', val.message);
            }
            break;

          case 'CLIENT':
            this.router.navigate(['/clients/dashboard']);
            break;

          default:
            this.router.navigate(['/']);
        }
      },error:err=>this.showmessage.show('error',err.message)
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    if (!control) return false; // ken controlName 8alet ou n"existe pas donc le chaamp ==> null
    return control.invalid && (control.dirty || control.touched);
  }
}
