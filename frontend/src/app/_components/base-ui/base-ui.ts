import { Component } from '@angular/core';
import { NavSide } from "../nav-side/nav-side";
import { HeaderComponent } from "../header-component/header-component";

@Component({
  selector: 'app-base-ui',
  imports: [NavSide, HeaderComponent],
  templateUrl: './base-ui.html',
  styleUrl: './base-ui.css',
})
export class BaseUi {}
