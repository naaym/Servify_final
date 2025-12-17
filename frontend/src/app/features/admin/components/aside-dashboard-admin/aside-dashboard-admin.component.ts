import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { AuthService } from '../../../auth/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'aside-dashboard-admin',
  imports: [CommonModule],
  templateUrl: './aside-dashboard-admin.html',
  styleUrl: './aside-dashboard-admin.scss',
})
export class AsideDashboardAdmin {
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);


  @Input() activeSection: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins' = 'dashboard';
  @Input() isSuperAdmin = false;
  @Output() sectionChange = new EventEmitter<'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins'>();

  navigate(section: 'dashboard' | 'providers' | 'clients' | 'bookings' | 'services' | 'admins') {
    if (section !== this.activeSection) {
      this.sectionChange.emit(section);
    }
  }
  logout(){
    this.authService.logout();
    this.router.navigate(['/'])
  }
}
