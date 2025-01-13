import React, { useState } from 'react';
import { createCave, updateCave } from '../../services/caveService';
import { Dialog, DialogContent, DialogTitle, TextField, Button, Grid, Box } from '@mui/material';

const CaveForm = ({ open, onClose, caveToEdit, makeApiRequest }) => {
  const [cave, setCave] = useState(
    caveToEdit || {
      coordinates: { x: 0, y: 0 },
    }
  );
  const [validateError, setValidateError] = useState({
    coordinates: { x: '', y: '' }
  })

  const validateFiled = (field, value) => {
    if (isNaN(value) || isNaN(parseFloat(value))) {
      setValidateError({ ...validateError, coordinates: { ...validateError.coordinates, [field]: "Invalid number" } });
      return false
    } else {
      setValidateError({ ...validateError, coordinates: { ...validateError.coordinates, [field]: "" } });
      return true
    }
  }

  const handleChange = (field, value) => {
    validateFiled(field, value)
    setCave({ ...cave, coordinates: { ...cave.coordinates, [field]: value } });
  };

  const handleSubmit = async () => {
    var isValid = validateFiled('x', cave.coordinates.x) && validateFiled('y', cave.coordinates.y)
    if (!isValid) return

    var response = null
    if (caveToEdit) {
      response = await makeApiRequest(updateCave, caveToEdit.id, cave);
    } else {
      response = await makeApiRequest(createCave, cave);
    }
    if (response != null) {
      onClose();
    }
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <Box>
        <DialogTitle>{caveToEdit ? 'Edit Cave' : 'Create New Cave'}</DialogTitle>
      </Box>
      <DialogContent>
        <Grid container spacing={2}>
          <Grid item xs={6}>
            <TextField
              required
              label="Coordinate X"
              value={cave.coordinates.x}
              onChange={(e) => handleChange('x', e.target.value)}
              error={validateError.coordinates.x != '' ? true : false}
              helperText={validateError.coordinates.x}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              required
              label="Coordinate Y"
              value={cave.coordinates.y}
              onChange={(e) => handleChange('y', e.target.value)}
              error={validateError.coordinates.y != '' ? true : false}
              helperText={validateError.coordinates.y}
              fullWidth
            />
          </Grid>
        </Grid>
        <Button
          variant="contained"
          color="primary"
          onClick={handleSubmit}
          sx={{ marginTop: 2 }}
        >
          {caveToEdit ? 'Update Cave' : 'Create Cave'}
        </Button>
      </DialogContent>
    </Dialog>
  );
};

export default CaveForm;