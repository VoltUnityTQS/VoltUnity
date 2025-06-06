// src/data/dashboardData.js

const dashboardData = {
  stations: [
    {
      id: 1,
      name: 'Estação Central',
      co2Saved: 520, // in kg
      totalProfit: 1340, // in €
      energyDelivered: 3720, // in kWh
      totalBookings: 78,
    },
    {
      id: 2,
      name: 'Estação Norte',
      co2Saved: 310,
      totalProfit: 980,
      energyDelivered: 2445,
      totalBookings: 54,
    },
    {
      id: 3,
      name: 'Estação Sul',
      co2Saved: 420,
      totalProfit: 1115,
      energyDelivered: 3090,
      totalBookings: 67,
    },
    {
      id: 4,
      name: 'Estação Oeste',
      co2Saved: 600,
      totalProfit: 1480,
      energyDelivered: 4000,
      totalBookings: 88,
    },
    {
      id: 5,
      name: 'Estação Leste',
      co2Saved: 285,
      totalProfit: 860,
      energyDelivered: 2135,
      totalBookings: 41,
    }
  ]
};

export default dashboardData;
