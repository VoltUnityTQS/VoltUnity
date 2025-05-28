import React from 'react';
import routes, { renderRoutes } from './routes';

const App = () => {
  return renderRoutes(routes);
};

export default App;