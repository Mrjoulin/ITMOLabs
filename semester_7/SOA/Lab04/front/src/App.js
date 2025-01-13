import React, { useState, useEffect } from 'react';
import { Container, Button, Typography, Box } from '@mui/material';
import DragonTable from './components/dragon/DragonTable';
import CaveTable from './components/cave/CaveTable';
import DragonForm from './components/dragon/DragonForm';
import CaveForm from './components/cave/CaveForm';
import KillerTable from './components/killer/KillerTable';
import { getCaves } from './services/caveService';
import { getDragons } from './services/dragonService';
import ApiError from './components/errors/ApiError';


function App() {
  const [openDragonForm, setOpenDragonForm] = useState(false);
  const [openCaveForm, setOpenCaveForm] = useState(false);
  const [dragonToEdit, setDragonToEdit] = useState(null);
  const [caveToEdit, setCaveToEdit] = useState(null);
  const [openApiError, setOpenApiError] = useState(false);
  const [apiError, setApiError] = useState(null);
  const [fetchDragons, setFetchDragons] = useState(false);
  const [fetchCaves, setFetchCaves] = useState(false);

  const handleOpenDragonForm = (dragon) => {
    setDragonToEdit(dragon);
    setOpenDragonForm(true);
  };

  const handleOpenCaveForm = (cave) => {
    setCaveToEdit(cave);
    setOpenCaveForm(true);
  };

  const handleCloseDragonForm = () => {
    setDragonToEdit(null);
    setOpenDragonForm(false);
    setFetchDragons(true);
  };

  const handleCloseCaveForm = () => {
    setCaveToEdit(null);
    setOpenCaveForm(false);
    setFetchCaves(true);
  };

  const handleCloseApiError = () => {
    setOpenApiError(false);
    setApiError(null);
  }

  const handleApiError = (error) => {
    setOpenApiError(true);
    setApiError(error);
  }

  const makeApiRequest = async (apiCall, ...args) => {
      try {
        const response = await apiCall(...args);
        if (response.status >= 200 && response.status < 300) {
            return response;
        } else {
            handleApiError(response.data);
        }
      } catch (error) {
        if (error.response) {
            handleApiError(error.response.data);
        } else {
            handleApiError({ message: error.message });
        }
      }
      return null
  };

  return (
    <Container maxWidth="lg" sx={{ textAlign: 'center', mt: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ fontWeight: 'bold', mb: 2 }}>
          D&D Management
        </Typography>
      </Box>

      {/* Dragon Section */}
      <Box sx={{ mb: 5 }}>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Dragons
        </Typography>
        <Button
          variant="contained"
          color="primary"
          onClick={() => handleOpenDragonForm(null)}
          sx={{ mb: 2 }}
        >
          Create New Dragon
        </Button>
        <Box sx={{ mt: 2 }}>
          <DragonTable onEdit={handleOpenDragonForm} makeApiRequest={makeApiRequest} fetchDragonsFlag={fetchDragons} />
        </Box>
      </Box>

      {/* Cave Section */}
      <Box sx={{ mb: 5 }}>
        <Typography variant="h5" sx={{ mb: 2 }}>
          Caves
        </Typography>
        <Button
          variant="contained"
          color="primary"
          onClick={() => handleOpenCaveForm(null)}
          sx={{ mb: 2 }}
        >
          Create New Cave
        </Button>
        <Box sx={{ mt: 2 }}>
          <CaveTable onEdit={handleOpenCaveForm} makeApiRequest={makeApiRequest} fetchCavesFlag={fetchCaves}/>
        </Box>
      </Box>

      {/* Killer Section */}
      <Box sx={{ mb: 5 }}>
        <Box sx={{ mt: 2 }}>
          <KillerTable makeApiRequest={makeApiRequest} />
        </Box>
      </Box>

      

      {/* Dragon Form Dialog */}
      {openDragonForm && (
        <DragonForm
          open={openDragonForm}
          onClose={handleCloseDragonForm}
          dragonToEdit={dragonToEdit}
          makeApiRequest={makeApiRequest}
        />
      )}

      {/* Cave Form Dialog */}
      {openCaveForm && (
        <CaveForm
          open={openCaveForm}
          onClose={handleCloseCaveForm}
          caveToEdit={caveToEdit}
          makeApiRequest={makeApiRequest}
        />
      )}
      <ApiError 
        open={openApiError}
        error={apiError}
        onClose={handleCloseApiError}
      />
    </Container>
  );
}

export default App;