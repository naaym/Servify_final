import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Landing } from './features/landing/landing';
import { Toast } from './shared/toast/toast.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,Toast],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'servify';
}
