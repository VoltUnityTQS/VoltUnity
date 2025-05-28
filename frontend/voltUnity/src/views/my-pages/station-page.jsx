import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { FiArrowLeft } from 'react-icons/fi';
import stations from '../../data/stations-enhanced';
import slotsData from '../../data/slots-enhanced';

const getColor = (status) => {
  if (status === 'available') return 'text-success';
  if (status === 'in_use') return 'text-warning';
  return 'text-danger';
};

const StationPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const stationId = parseInt(id);
  const station = stations.find(s => s.id === stationId);
  const slots = slotsData[stationId] || [];

  if (!station) return <div>Estação não encontrada.</div>;

  return (
    <Card>
      <Card.Header className="d-flex align-items-center gap-2 justify-content-between">
        <Card.Title as="h5" className="mb-0">{station.nome} ({station.distancia} km)</Card.Title>
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
                <td><MdCircle className={getColor(slot.status)} /></td>
                <td>Slot #{slot.id}</td>
                <td>{slot.capacidadeKW} kW</td>
                <td>{slot.rapido ? 'Sim' : 'Não'}</td>
                <td><Button variant="primary">Reservar</Button></td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Card.Body>
    </Card>
  );
};

export default StationPage;