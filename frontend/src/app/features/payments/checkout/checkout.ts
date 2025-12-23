import { AfterViewInit, Component, ElementRef, ViewChild, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { loadStripe, Stripe, StripeElements, StripeCardElement } from '@stripe/stripe-js';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../services/payment.service';
import { ShowMessageService } from '../../../shared/services/showmessage.service';
import { ClientBookingService } from '../../client/pages/bookings/clientbooking.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html',
  styleUrls: [],
})
export class CheckoutComponent implements AfterViewInit {
  @ViewChild('cardElement') cardElementRef?: ElementRef<HTMLDivElement>;

  private paymentService = inject(PaymentService);
  private messageService = inject(ShowMessageService);
  private bookingService = inject(ClientBookingService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  stripe: Stripe | null = null;
  elements: StripeElements | null = null;
  card: StripeCardElement | null = null;
  processing = false;
  statusMessage = '';
  defaultAmount = '30.00';
  resolvedAmount = '30.00';
  currency = 'eur';
  orderId?: number;

  async ngAfterViewInit(): Promise<void> {
    const bookingIdParam = this.route.snapshot.queryParamMap.get('bookingId');
    if (bookingIdParam) {
      const parsedId = Number(bookingIdParam);
      this.orderId = Number.isNaN(parsedId) ? undefined : parsedId;
    }
    this.paymentService.getConfig().subscribe({
      next: async (config) => {
        this.defaultAmount = config.defaultAmount;
        this.resolvedAmount = config.defaultAmount;
        this.currency = config.currency;
        if (!config.publishableKey) {
          this.messageService.show('error', 'Clé Stripe publishable manquante.');
          return;
        }
        this.stripe = await loadStripe(config.publishableKey);
        if (!this.stripe) {
          this.messageService.show('error', 'Stripe.js indisponible.');
          return;
        }
        this.elements = this.stripe.elements();
        this.card = this.elements.create('card');
        this.card.mount(this.cardElementRef?.nativeElement as HTMLElement);
      },
      error: () => {
        this.messageService.show('error', 'Impossible de charger la configuration Stripe.');
      },
    });

    if (this.orderId) {
      this.loadBookingAmount(this.orderId);
    }
  }

  onOrderIdChange(value: number | null) {
    this.orderId = value ?? undefined;
    if (this.orderId) {
      this.loadBookingAmount(this.orderId);
    } else {
      this.resolvedAmount = this.defaultAmount;
    }
  }

  private loadBookingAmount(orderId: number) {
    this.bookingService.getBookingsById(orderId).subscribe({
      next: (booking) => {
        const basePrice = booking.providerInfo?.basePrice;
        if (basePrice !== null && basePrice !== undefined) {
          this.resolvedAmount = basePrice.toFixed(2);
        } else {
          this.resolvedAmount = this.defaultAmount;
        }
      },
      error: () => {
        this.resolvedAmount = this.defaultAmount;
      },
    });
  }

  pay(): void {
    if (this.processing) {
      return;
    }
    if (!this.stripe || !this.card) {
      this.messageService.show('error', 'Stripe n’est pas prêt.');
      return;
    }
    if (!this.orderId) {
      this.messageService.show('error', 'Veuillez renseigner un bookingId.');
      return;
    }
    this.processing = true;
    this.statusMessage = 'Création du paiement...';
    this.paymentService.createPaymentIntent(this.orderId).subscribe({
      next: async (response) => {
        this.statusMessage = 'Confirmation du paiement...';
        const result = await this.stripe?.confirmCardPayment(response.clientSecret, {
          payment_method: { card: this.card as StripeCardElement },
        });

        if (result?.error) {
          this.statusMessage = 'Paiement échoué.';
          this.messageService.show('error', result.error.message || 'Paiement échoué.');
        } else {
          this.statusMessage = 'Paiement en cours de confirmation...';
          this.messageService.show('success', 'Paiement envoyé, en attente de confirmation.');
          this.router.navigate(['/clients/dashboard']);
        }
        this.processing = false;
      },
      error: () => {
        this.processing = false;
        this.statusMessage = 'Impossible de créer le paiement.';
        this.messageService.show('error', 'Erreur lors de la création du PaymentIntent.');
      },
    });
  }
}
