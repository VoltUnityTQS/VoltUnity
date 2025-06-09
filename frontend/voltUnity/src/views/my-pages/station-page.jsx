// src/views/my-pages/station-page.jsx

import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { FiArrowLeft } from 'react-icons/fi';
import { createBooking, getStationSlots } from '../../services/api';

const getColor = (status) => {
    if (status === 'AVAILABLE') return 'text-success';
    if (status === 'IN_USE') return 'text-warning';
    return 'text-danger';
};

const StationPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const stationId = parseInt(id);

    const [station, setStation] = useState(null);
    const [slots, setSlots] = useState([]);

    // Modal states
    const [showBookingModal, setShowBookingModal] = useState(false);
    const [selectedSlotId, setSelectedSlotId] = useState(null);
    const [bookingStartTime, setBookingStartTime] = useState('');
    const [bookingEndTime, setBookingEndTime] = useState('');
    const [estimatedPrice, setEstimatedPrice] = useState(0);

    // Mock user (ou usa o real se tiveres auth)
    const user = { id: 1 };

    useEffect(() => {
        async function fetchStation() {
            try {
                const data = await getStationSlots(stationId);
                setSlots(data);

                // Usar a station que vem dos slots
                if (data.length > 0 && data[0].station) {
                    setStation({
                        id: data[0].station.id,
                        nome: data[0].station.name,
                        distancia: 0,
                        pricePerKwh: data[0].station.pricePerKWh
                    });
                } else {
                    setStation({
                        id: stationId,
                        nome: `Estação ${stationId}`,
                        distancia: 0,
                        pricePerKwh: 0.3
                    });
                }
            } catch (error) {
                console.error('Erro ao buscar slots da estação:', error);
            }
        }

        fetchStation();
    }, [stationId]);

    const openBookingModal = (slotId) => {
        setSelectedSlotId(slotId);
        setBookingStartTime('');
        setBookingEndTime('');
        setEstimatedPrice(0);
        setShowBookingModal(true);
    };

    const updateEstimatedPrice = (startTime, endTime) => {
        if (!startTime || !endTime) {
            setEstimatedPrice(0);
            return;
        }

        const now = new Date();
        const todayStr = now.toISOString().split('T')[0];

        const start = new Date(`${todayStr}T${startTime}`);
        const end = new Date(`${todayStr}T${endTime}`);

        if (end <= start) {
            setEstimatedPrice(0);
            return;
        }

        const durationInHours = (end - start) / (1000 * 60 * 60);
        const price = durationInHours * 10 * station.pricePerKwh;
        setEstimatedPrice(price.toFixed(2));
    };

    const handleBooking = async () => {
        try {
            const now = new Date();
            const todayStr = now.toISOString().split('T')[0];

            const startDate = new Date(`${todayStr}T${bookingStartTime}`);
            const endDate = new Date(`${todayStr}T${bookingEndTime}`);

            const bookingData = {
                slotId: selectedSlotId,
                userId: user.id,
                start: startDate.toISOString(),
                end: endDate.toISOString()
            };

            await createBooking(bookingData);
            alert(`Reserva criada para Slot ${selectedSlotId}!`);
            setShowBookingModal(false);
        } catch (error) {
            console.error('Erro ao criar booking:', error);
            alert('Erro ao criar booking.');
        }
    };

    if (!station) return <div>Estação não encontrada.</div>;

    return (
        <Card>
            <Card.Header className="d-flex align-items-center gap-2 justify-content-between">
                <Card.Title as="h5" className="mb-0">
                    {station.nome} ({station.distancia} km)
                </Card.Title>
            </Card.Header>
            <Card.Body>
                <Button variant="secondary" onClick={() => navigate(-1)} className="mb-3">
                    <FiArrowLeft className="me-1" /> Voltar
                </Button>
                <Table hover>
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th>Slot</th>
                            <th>Capacidade (kW)</th>
                            <th>Rápido</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {slots.map((slot) => (
                            <tr key={slot.id}>
                                <td><MdCircle className={getColor(slot.slotStatus)} /></td>
                                <td>Slot #{slot.slotNumber}</td>
                                <td>{slot.power} kW</td>
                                <td>{slot.type?.toLowerCase().includes('fast') || slot.type?.toLowerCase().includes('dc') ? 'Sim' : 'Não'}</td>
                                <td>
                                    <Button
                                        variant="primary"
                                        disabled={slot.slotStatus !== 'AVAILABLE'}
                                        onClick={() => openBookingModal(slot.id)}
                                    >
                                        {slot.slotStatus === 'AVAILABLE' ? 'Reservar' : 'Indisponível'}
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Card.Body>

            {/* Modal de booking */}
            <Modal show={showBookingModal} onHide={() => setShowBookingModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Nova Reserva - Slot #{selectedSlotId}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Hora de início</Form.Label>
                            <Form.Control
                                type="time"
                                value={bookingStartTime}
                                min={new Date().toISOString().slice(11, 16)}
                                onChange={(e) => {
                                    setBookingStartTime(e.target.value);
                                    updateEstimatedPrice(e.target.value, bookingEndTime);
                                }}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Hora de fim</Form.Label>
                            <Form.Control
                                type="time"
                                value={bookingEndTime}
                                min={bookingStartTime || new Date().toISOString().slice(11, 16)}
                                onChange={(e) => {
                                    setBookingEndTime(e.target.value);
                                    updateEstimatedPrice(bookingStartTime, e.target.value);
                                }}
                            />
                        </Form.Group>
                        <div><strong>Preço estimado:</strong> {estimatedPrice} €</div>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowBookingModal(false)}>
                        Cancelar
                    </Button>
                    <Button variant="primary" onClick={handleBooking} disabled={!bookingStartTime || !bookingEndTime}>
                        Confirmar Reserva
                    </Button>
                </Modal.Footer>
            </Modal>
        </Card>
    );
};

export default StationPage;