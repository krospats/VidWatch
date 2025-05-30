import React from 'react';
import { Navbar as BootstrapNavbar, Nav, Container, Button } from 'react-bootstrap';
import { useNavigate, useLocation } from 'react-router-dom';

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleHomeClick = () => {
    navigate('/');
  };

  const handleUsersClick = () => {
    navigate('/users');
  };

  return (
      <BootstrapNavbar
          bg="dark"
          variant="dark"
          expand="lg"
          style={{ minHeight: '50px', height: '50px' }}
      >
        <Container style={{ height: '50px' }}>
          <BootstrapNavbar.Brand
              onClick={handleHomeClick}
              style={{ cursor: 'pointer', lineHeight: '50px' }}
          >
            Video Manager
          </BootstrapNavbar.Brand>
          <BootstrapNavbar.Toggle
              aria-controls="basic-navbar-nav"
              style={{ height: '50px', padding: '0 0px', width: '125%' }}
          />
          <BootstrapNavbar.Collapse id="basic-navbar-nav" style={{ height: '50px' }}>
            <Nav className="me-auto" style={{ height: '50px', alignItems: 'center' }}>
              <Button
                  variant="outline-light"
                  className="me-2"
                  onClick={handleHomeClick}
                  active={location.pathname === '/'}
                  style={{ height: '38px' }}
              >
                Видео
              </Button>
              <Button
                  variant="outline-light"
                  className="me-2"
                  onClick={handleUsersClick}
                  active={location.pathname === '/users'}
                  style={{ height: '38px' }}
              >
                Пользователи
              </Button>
            </Nav>
          </BootstrapNavbar.Collapse>
        </Container>
      </BootstrapNavbar>
  );
};

export default Navbar;