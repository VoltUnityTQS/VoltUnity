// src/views/dashboard/index.jsx

import React, { useEffect, useState } from 'react';
import { Row, Col, Card, Table } from 'react-bootstrap';
import { Bar, Pie } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    ArcElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';

import {
    getDashboardMetrics,
    getTotalBookings,
    getSlotStatusSummary,
    getBookingsPerStation,
    getRecentBookings
} from '../../services/api';

// Registrar Chart.js components
ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    ArcElement,
    Title,
    Tooltip,
    Legend
);

const DashboardPage = () => {
    // States
    const [metrics, setMetrics] = useState({
        co2Saved: 0,
        totalRevenue: 0,
        totalEnergy: 0
    });
    const [totalBookings, setTotalBookings] = useState(0);
    const [slotStatusSummary, setSlotStatusSummary] = useState({});
    const [bookingsPerStation, setBookingsPerStation] = useState({});
    const [recentBookings, setRecentBookings] = useState([]);

    // Fetch data
    useEffect(() => {
        async function fetchData() {
            try {
                const metricsData = await getDashboardMetrics();
                setMetrics(metricsData);

                const totalBookingsData = await getTotalBookings();
                setTotalBookings(totalBookingsData);

                const slotStatusData = await getSlotStatusSummary();
                setSlotStatusSummary(slotStatusData);

                // Se não tiveres /stations/occupancy podes comentar isto!
                const bookingsPerStationData = await getBookingsPerStation();
                setBookingsPerStation(bookingsPerStationData);

                const recentBookingsData = await getRecentBookings();
                setRecentBookings(recentBookingsData);
            } catch (error) {
                console.error('Erro ao buscar dados do dashboard:', error);
            }
        }

        fetchData();
    }, []);

    // Chart data → Slot Status Pie Chart
    const pieData = {
        labels: ['Available', 'In Use', 'Maintenance'],
        datasets: [
            {
                data: [
                    slotStatusSummary.available || 0,
                    slotStatusSummary.in_use || 0,
                    slotStatusSummary.maintenance || 0
                ],
                backgroundColor: ['#28a745', '#ffc107', '#dc3545']
            }
        ]
    };

    // Chart data → Bookings per Station Bar Chart
    const barData = {
        labels: Object.keys(bookingsPerStation),
        datasets: [
            {
                label: 'Bookings',
                data: Object.values(bookingsPerStation),
                backgroundColor: 'rgba(54, 162, 235, 0.6)'
            }
        ]
    };

    return (
        <>
            <h2>Dashboard</h2>

            {/* KPIs */}
            <Row>
                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>CO₂ Poupado</Card.Title>
                            <Card.Text>{metrics.co2Saved} kg</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Receita Total</Card.Title>
                            <Card.Text>{metrics.totalRevenue} €</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Energia Total</Card.Title>
                            <Card.Text>{metrics.totalEnergy} kWh</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Total Bookings</Card.Title>
                            <Card.Text>{totalBookings}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Pie Chart - Slot Status */}
            <Row className="mt-4">
                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Estado dos Slots</Card.Title>
                            <Pie data={pieData} />
                        </Card.Body>
                    </Card>
                </Col>

                {/* Bar Chart - Bookings per Station */}
                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Bookings por Estação</Card.Title>
                            <Bar data={barData} />
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Recent Bookings Table */}
            <Row className="mt-4">
                <Col>
                    <Card>
                        <Card.Body>
                            <Card.Title>Últimos Bookings</Card.Title>
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Estação</th>
                                        <th>Slot</th>
                                        <th>User</th>
                                        <th>Timestamp</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {recentBookings.map((booking) => (
                                        <tr key={booking.id}>
                                            <td>{booking.id}</td>
                                            <td>{booking.stationName}</td>
                                            <td>{booking.slotId}</td>
                                            <td>{booking.user}</td>
                                            <td>{new Date(booking.timestamp).toLocaleString()}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </>
    );
};

export default DashboardPage;