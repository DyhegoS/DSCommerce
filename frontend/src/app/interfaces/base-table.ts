import { TemplateRef } from "@angular/core";

export interface BaseTable {
  header: string;
  field: string;
  template?: TemplateRef<any>;
}
