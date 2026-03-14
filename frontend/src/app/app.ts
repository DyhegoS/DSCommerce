import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from './_components/header/header';
import { Sidebar } from './_components/sidebar/sidebar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Sidebar],

  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('reize-commerce');
}
