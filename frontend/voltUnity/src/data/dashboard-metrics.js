const dashboardMetrics = {
  energyDelivered: [
    { month: 'Jan', kWh: 420 },
    { month: 'Feb', kWh: 510 },
    { month: 'Mar', kWh: 630 },
    { month: 'Apr', kWh: 580 },
    { month: 'May', kWh: 720 },
    { month: 'Jun', kWh: 690 }
  ],
  chargingType: {
    fast: 60,
    slow: 40
  },
  co2SavedKg: 14520,
  revenue: [
    { label: 'Jan', profit: 1800 },
    { label: 'Feb', profit: 2100 },
    { label: 'Mar', profit: 2300 },
    { label: 'Apr', profit: 2200 },
    { label: 'May', profit: 2500 },
    { label: 'Jun', profit: 2600 }
  ]
};

export default dashboardMetrics;