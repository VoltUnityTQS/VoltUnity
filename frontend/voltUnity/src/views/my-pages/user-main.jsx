import React, { useEffect, useState } from 'react';
import { Row, Col, Table, Card, Form } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import { getStations, getStationSlots } from '../../services/api';

// Função para calcular distância com fórmula de Haversine
const calcularDistanciaKm = (lat1, lon1, lat2, lon2) => {
    const R = 6371; // km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
        Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
};

// Coords de Aveiro
const userLat = 40.6405;
const userLon = -8.6538;

const getColor = (status) => {
    if (status === 'available') return 'text-success';
    if (status === 'in_use') return 'text-warning';
    return 'text-danger';
};

const UserMain = () => {
    const [ordenarPor, setOrdenarPor] = useState('nome');
    const [filtrarRapido] = useState(false);
    const [filtrarDisponivel] = useState(false);
    const [stations, setStations] = useState([]);

    const navigate = useNavigate();

    // Fetch stations from backend
    useEffect(() => {
        async function fetchStations() {
            try {
                const data = await getStations();

                // Para cada estação → buscar os slots
        const stationsWithSlots = await Promise.all(
            data.map(async (st) => {
                try {
                    const slots = await getStationSlots(st.id);

                    const lat = parseFloat(st.lat);
                    const lon = parseFloat(st.lng);

                    return {
                        id: st.id,
                        nome: st.name,
                        distancia: (isNaN(lat) || isNaN(lon))
                            ? 0
                            : calcularDistanciaKm(userLat, userLon, lat, lon),
                        rapido: slots.some(slot => slot.type && (slot.type.toUpperCase().includes('DC') || slot.type.toUpperCase().includes('FAST'))),
                        status: slots.some(slot => slot.slotStatus === 'AVAILABLE')
                            ? 'available'
                            : slots.every(slot => slot.slotStatus === 'MAINTENANCE')
                            ? 'maintenance'
                            : 'in_use'
                    };
                } catch (err) {
                    console.error(`Erro ao buscar slots da estação ${st.id}:`, err);

                    const lat = parseFloat(st.latitude);
                    const lon = parseFloat(st.longitude);

                    return {
                        id: st.id,
                        nome: st.name,
                        distancia: (isNaN(lat) || isNaN(lon))
                            ? 0
                            : calcularDistanciaKm(userLat, userLon, lat, lon),
                        rapido: false,
                        status: 'in_use'
                    };
                }
            })
        );

                setStations(stationsWithSlots);
            } catch (error) {
                console.error('Erro ao buscar estações:', error);
            }
        }

        fetchStations();
    }, []);

    // Filtros e ordenação
    let filteredStations = [...stations];

    if (filtrarRapido) {
        filteredStations = filteredStations.filter(st => st.rapido);
    }

    if (filtrarDisponivel) {
        filteredStations = filteredStations.filter(st => st.status === 'available');
    }

    filteredStations.sort((a, b) => {
        return ordenarPor === 'distancia'
            ? a.distancia - b.distancia
            : a.nome.localeCompare(b.nome);
    });

    return (
        <Card>
            <Card.Header>
                <Card.Title as="h5">Procurar Estações</Card.Title>
                <Form inline className="d-flex gap-2 mt-2">
                    <Form.Select onChange={(e) => setOrdenarPor(e.target.value)} defaultValue="nome">
                        <option value="nome">Ordenar por Nome</option>
                        <option value="distancia">Ordenar por Distância</option>
                    </Form.Select>
                </Form>
            </Card.Header>
            <Card.Body>
                <Table hover>
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th>Nome</th>
                            <th>Distância (km)</th>
                            <th>Carregamento Rápido</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredStations.map((station) => (
                            <tr
                                key={station.id}
                                style={{ cursor: 'pointer' }}
                                onClick={() => navigate(`/station/${station.id}`)}
                            >
                                <td><MdCircle className={getColor(station.status)} /></td>
                                <td>{station.nome}</td>
                                <td>{station.distancia.toFixed(1)}</td>
                                <td>{station.rapido ? 'Sim' : 'Não'}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Card.Body>
        </Card>
    );
};

export default UserMain;