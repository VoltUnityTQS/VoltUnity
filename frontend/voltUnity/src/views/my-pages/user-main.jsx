// src/views/my-pages/user-main.jsx

import React, { useEffect, useState } from 'react';
import { Row, Col, Table, Card, Form } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import { getStations } from '../../services/api';

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
                // Adaptar: o backend devolve "name", slots e estado calculável
                const stationsAdapted = data.map(st => ({
                    id: st.id,
                    nome: st.name,
                    distancia: st.distance || 0, // se tiveres esta info, se não, podes deixar 0
                    rapido: st.slots.some(slot => slot.fast), // se algum slot for rápido
                    status: st.slots.some(slot => slot.slotStatus === 'available')
                        ? 'available'
                        : st.slots.every(slot => slot.slotStatus === 'maintenance')
                        ? 'maintenance'
                        : 'in_use'
                }));
                setStations(stationsAdapted);
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
                                <td>{station.distancia}</td>
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