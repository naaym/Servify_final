import { Component } from '@angular/core';
import { RouterLink } from "@angular/router";
import { ServiceIcon } from './service-icon/service-icon';

@Component({
  selector: 'app-service',
  imports: [RouterLink,ServiceIcon],
  templateUrl: './service.html',
  styleUrl: './service.scss',
})
export class Service {

}
