import { Component, EventEmitter, Input, OnChanges, OnDestroy, Output, SimpleChanges, inject } from '@angular/core';
import { finalize, Subject, takeUntil } from 'rxjs';
import { SearchOptionsService } from '../../../Search/services/search-options.service';

@Component({
  selector: 'app-service',
  imports: [],
  templateUrl: './service.modal.html',
  styleUrl: './service.modal.scss',
})
export class ServiceModal implements OnChanges, OnDestroy {
  @Input({required:true}) open:boolean=false;
  @Output() close = new EventEmitter();
  @Output() selectService = new EventEmitter<string>();

  services: string[] = [];
  isLoading = false;
  private readonly destroy$ = new Subject<void>();
  private readonly searchOptionsService = inject(SearchOptionsService);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['open']?.currentValue === true) {
      this.loadServices();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadServices(): void {
    this.isLoading = true;
    this.searchOptionsService
      .getAvailableServices()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.isLoading = false))
      )
      .subscribe({
        next: (services) => (this.services = services ?? []),
        error: () => (this.services = []),
      });
  }
}
