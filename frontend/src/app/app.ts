import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginPage } from './pages/login-page/login-page';
import { BaseUi } from './_components/base-ui/base-ui';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, BaseUi],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
