import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import { ConfigProvider } from './contexts/ConfigContext';

import './index.scss';
import App from './App';
import reportWebVitals from './reportWebVitals';

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <BrowserRouter basename={import.meta.env.VITE_APP_BASE_NAME}>
    <ConfigProvider>
      <App />
    </ConfigProvider>
  </BrowserRouter>
);

reportWebVitals();