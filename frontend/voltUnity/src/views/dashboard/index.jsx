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

import api from '../../services/api';

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
    // STATES
    const [co2Saved, setCo2Saved] = useState(0);
    const [totalRevenue, setTotalRevenue] = useState(0);
    const [totalEnergy, setTotalEnergy] = useState(0);
    const [totalSlots, setTotalSlots] = useState(0);
    const [slotStatusSummary, setSlotStatusSummary] = useState({
        available: 0,
        in_use: 0,
        maintenance: 0
    });
    const [slotsInUsePerStation, setSlotsInUsePerStation] = useState({});
    const [recentBookings, setRecentBookings] = useState([]);

    useEffect(() => {
        async function fetchData() {
            try {
                // STATIONS
                const stationsRes = await api.get('/api/v1/stations');
                const stations = stationsRes.data;

                // Total slots
                const totalSlotsCount = stations.reduce((sum, station) => sum + station.slots.length, 0);
                setTotalSlots(totalSlotsCount);

                // Slot status summary
                let available = 0, in_use = 0, maintenance = 0;

                stations.forEach(station => {
                    station.slots.forEach(slot => {
                        const status = slot.slotStatus.toLowerCase();
                        if (status === 'available') available++;
                        else if (status === 'in_use') in_use++;
                        else if (status === 'maintenance') maintenance++;
                    });
                });

                setSlotStatusSummary({ available, in_use, maintenance });

                // Slots in use per station
                const slotsPerStation = {};
                stations.forEach(station => {
                    const inUseCount = station.slots.filter(slot => slot.slotStatus.toLowerCase() === 'in_use').length;
                    slotsPerStation[station.name] = inUseCount;
                });
                setSlotsInUsePerStation(slotsPerStation);

                // PAYMENTS → Receita total
                const paymentsRes = await api.get('/api/v1/payments');
                const payments = paymentsRes.data;
                const revenue = payments
                    .filter(payment => payment.paymentStatus === 'COMPLETED')
                    .reduce((sum, payment) => sum + payment.amount, 0);
                setTotalRevenue(revenue);

                // CHARGING SESSIONS → Energia total & CO2 saved
                const sessionsRes = await api.get('/api/v1/charging-sessions');
                const sessions = sessionsRes.data;
                const totalEnergyKwh = sessions.reduce((sum, session) => sum + session.energyConsumedkwh, 0);
                setTotalEnergy(totalEnergyKwh);

                const co2Factor = 0.3; // kg CO2 per kWh (example factor)
                setCo2Saved(totalEnergyKwh * co2Factor);

                // BOOKINGS → últimos bookings
                const bookingsRes = await api.get('/api/v1/bookings');
                const bookings = bookingsRes.data;

                // ordenar DESC por start e pegar últimos 5
                const sortedBookings = bookings
                    .sort((a, b) => new Date(b.start) - new Date(a.start))
                    .slice(0, 5);

                setRecentBookings(sortedBookings);

            } catch (error) {
                console.error('Erro ao buscar dados do dashboard:', error);
            }
        }

        fetchData();
    }, []);

    // CHART DATA - Pie
    const pieData = {
        labels: ['Available', 'In Use', 'Maintenance'],
        datasets: [
            {
                data: [
                    slotStatusSummary.available,
                    slotStatusSummary.in_use,
                    slotStatusSummary.maintenance
                ],
                backgroundColor: ['#28a745', '#ffc107', '#dc3545']
            }
        ]
    };

    // CHART DATA - Bar
    const barData = {
        labels: Object.keys(slotsInUsePerStation),
        datasets: [
            {
                label: 'Slots em uso',
                data: Object.values(slotsInUsePerStation),
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
                            <Card.Text>{co2Saved.toFixed(1)} kg</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Receita Total</Card.Title>
                            <Card.Text>{totalRevenue.toFixed(2)} €</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Energia Total</Card.Title>
                            <Card.Text>{totalEnergy.toFixed(1)} kWh</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={3}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Total Slots</Card.Title>
                            <Card.Text>{totalSlots}</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Charts */}
            <Row className="mt-4">
                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Estado dos Slots</Card.Title>
                            <Pie data={pieData} />
                        </Card.Body>
                    </Card>
                </Col>

                <Col md={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title>Slots em uso por Estação</Card.Title>
                            <Bar data={barData} />
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            {/* Recent bookings */}
            <Row className="mt-4">
                <Col>
                    <Card>
                        <Card.Body>
                            <Card.Title>Últimos Bookings</Card.Title>
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Slot</th>
                                        <th>User</th>
                                        <th>Start</th>
                                        <th>End</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {recentBookings.map((booking) => (
                                        <tr key={booking.id}>
                                            <td>{booking.id}</td>
                                            <td>{booking.slotId}</td>
                                            <td>{booking.userId}</td>
                                            <td>{new Date(booking.start).toLocaleString()}</td>
                                            <td>{new Date(booking.end).toLocaleString()}</td>
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