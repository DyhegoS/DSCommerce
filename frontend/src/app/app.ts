import { Component, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Header } from './components/header/header';
import { Sidenav } from './components/sidenav/sidenav';
import { Login } from './pages/login/login';
import { AuthService } from './services/auth-service';

@Component({
  selector: 'app-root',
  imports: [RouterModule, Header, Sidenav, Login],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');

  private authService = inject(AuthService);

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }
}
