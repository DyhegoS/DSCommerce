import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import {
  provideHttpClient,
  withInterceptors,
  HTTP_INTERCEPTORS,
  withFetch,
} from '@angular/common/http';

import { routes } from './app.routes';
import { MatPaginatorIntl } from '@angular/material/paginator';

function customPaginatorIntl() {
  const intl = new MatPaginatorIntl();
  intl.itemsPerPageLabel = 'Itens por página:';
  intl.nextPageLabel = 'Próxima página';
  intl.previousPageLabel = 'Página anterior';
  intl.firstPageLabel = 'Primeira página';
  intl.lastPageLabel = 'Última página';
  intl.getRangeLabel = (page: number, pageSize: number, length: number) => {
    if (length === 0) return '0 de 0';
    const totalPages = Math.ceil(length / pageSize);
    return `Página ${page + 1} de ${totalPages}`;
  };
  return intl;
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([]), withFetch()),
    provideZoneChangeDetection({ eventCoalescing: true }),
    { provide: MatPaginatorIntl, useFactory: customPaginatorIntl },
  ],
};
