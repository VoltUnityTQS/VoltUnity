import React from 'react';
import { Card } from 'react-bootstrap';

const UserDashboard = () => {
    const userName = localStorage.getItem('userName') || 'Utilizador';

    return (
        <Card>
            <Card.Header>
                <Card.Title as="h5">Meu Dashboard</Card.Title>
            </Card.Header>
            <Card.Body>
                <h6>Bem-vindo, {userName}!</h6>
                <p>Este é o seu painel pessoal.</p>
                <p>Aqui poderá futuramente ver as suas estatísticas, histórico de carregamentos, faturação, etc.</p>
            </Card.Body>
        </Card>
    );
};

export default UserDashboard;