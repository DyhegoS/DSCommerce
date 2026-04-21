import { Component, signal, ViewChild } from '@angular/core';
import { MatSidenavContainer, MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-sidenav',
  imports: [MatSidenavModule, RouterOutlet, MatListModule, MatIconModule, RouterLink],
  templateUrl: './sidenav.html',
  styleUrl: './sidenav.css',
})
export class Sidenav {
  @ViewChild('container') container!: MatSidenavContainer;

  expanded = signal(false);

  onMouseEnter() {
    this.expanded.set(true);
    setTimeout(() => this.container.updateContentMargins(), 0);
  }

  onMouseLeave() {
    this.expanded.set(false);
    setTimeout(() => this.container.updateContentMargins(), 0);
  }
}
