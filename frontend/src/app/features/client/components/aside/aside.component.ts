import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-aside',
  imports: [RouterModule],
  templateUrl: './aside.component.html',
  styleUrl: './aside.component.scss',
})
export class AsideComponent {
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);

    logout(){
    this.authService.logout();
    this.router.navigate(['/'])
  }

}
