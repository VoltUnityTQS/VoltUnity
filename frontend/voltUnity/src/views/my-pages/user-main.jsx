import React, { useState } from 'react';
import { Row, Col, Table, Card, Form } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import stations from '../../data/stations-enhanced';

const getColor = (status) => {
  if (status === 'available') return 'text-success';
  if (status === 'in_use') return 'text-warning';
  return 'text-danger';
};

const UserMain = () => {
  const [ordenarPor, setOrdenarPor] = useState('nome');
  const [filtrarRapido] = useState(false);
  const [filtrarDisponivel] = useState(false);
  const navigate = useNavigate();

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
              <tr key={station.id} style={{ cursor: 'pointer' }} onClick={() => navigate(`/station/${station.id}`)}>
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