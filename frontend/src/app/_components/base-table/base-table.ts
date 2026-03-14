import { CommonModule } from '@angular/common';
import { BaseTable } from './../../interfaces/base-table';
import { Component, Input, TemplateRef } from '@angular/core';

@Component({
  selector: 'app-base-table',
  imports: [CommonModule],
  templateUrl: './base-table.html',
  styleUrl: './base-table.css',
})
export class BaseTableComponent {
  @Input() data: any[] = [];
  @Input() columns: BaseTable[] = [];

  @Input() customTemplates: { [key: string]: TemplateRef<any> } = {};

  getTemplate(fieldName: string): TemplateRef<any> | null {
    return this.customTemplates[fieldName] || null;
  }
}
