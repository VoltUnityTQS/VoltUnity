import React from 'react';
import { Row, Col, Card } from 'react-bootstrap';
import { Bar, Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Tooltip,
  Legend
} from 'chart.js';

import dashboardData from './dashboardData';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Tooltip,
  Legend
);

const DashDefault = () => {
  const labels = dashboardData.map((station) => station.name);

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top'
      },
      title: {
        display: false
      }
    }
  };

  const createChartData = (label, dataKey, backgroundColor) => ({
    labels,
    datasets: [
      {
        label,
        data: dashboardData.map((station) => station[dataKey]),
        backgroundColor
      }
    ]
  });

  return (
    <React.Fragment>
      <Row>
        <Col md={6} xl={6}>
          <Card>
            <Card.Header>
              <Card.Title as="h5">CO₂ Poupado (kg)</Card.Title>
            </Card.Header>
            <Card.Body>
              <Bar options={chartOptions} data={createChartData('CO₂ Poupado', 'co2Saved', 'rgba(75, 192, 192, 0.6)')} />
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} xl={6}>
          <Card>
            <Card.Header>
              <Card.Title as="h5">Lucros (€)</Card.Title>
            </Card.Header>
            <Card.Body>
              <Bar options={chartOptions} data={createChartData('Lucros', 'profit', 'rgba(153, 102, 255, 0.6)')} />
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} xl={6}>
          <Card>
            <Card.Header>
              <Card.Title as="h5">Energia Fornecida (kWh)</Card.Title>
            </Card.Header>
            <Card.Body>
              <Line
                options={chartOptions}
                data={createChartData('Energia Fornecida', 'energyDelivered', 'rgba(255, 206, 86, 0.6)')}
              />
            </Card.Body>
          </Card>
        </Col>

        <Col md={6} xl={6}>
          <Card>
            <Card.Header>
              <Card.Title as="h5">Reservas</Card.Title>
            </Card.Header>
            <Card.Body>
              <Line
                options={chartOptions}
                data={createChartData('Reservas', 'bookings', 'rgba(255, 99, 132, 0.6)')}
              />
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </React.Fragment>
  );
};

export default DashDefault;