import React, { useEffect, useState } from 'react';
import { createDragon, updateDragon } from '../../services/dragonService';
import { Dialog, DialogContent, DialogTitle, TextField, Button, Grid, Select, MenuItem } from '@mui/material';
import { DragonTypes, DragonCharacters, Countries, Colors } from  './EnumConsts';


function toTitleCase(str) {
  return str.replace('_', ' ').replace(
    /\w\S*/g,
    text => text.charAt(0).toUpperCase() + text.substring(1).toLowerCase()
  );
}


const DragonForm = ({ open, onClose, dragonToEdit, makeApiRequest }) => {
  const [dragon, setDragon] = useState(
    dragonToEdit || {
      name: '',
      coordinates: { x: 0, y: 0 },
      age: 1,
      color: '',
      type: '',
      character: '',
      killer: { name: '', weight: '', eyeColor: '', nationality: '' },
      caveId: '',
    }
  );
  const [validateError, setValidateError] = useState({
    name: '',
    coordinates: { x: '', y: '' },
    age: '',
    color: '',
    type: '',
    character: '',
    killer: { name: '', weight: '', eyeColor: '', nationality: '' },
    caveId: ''
  })

  if (dragon.killer == null) {
    console.log("Set dragon deafult killer")
    setDragon({ ...dragon, killer: { name: '', weight: '', eyeColor: '', nationality: '' } })
  }
  
  const validateFiled = (field, value) => {
    if (field == 'age' || field == 'caveId') {
      if (isNaN(value) || !(/^\+?([1-9]\d*)$/.test(value))) {
        setValidateError({ ...validateError, [field]: "Invalid integer" });
        return false
      } else {
        setValidateError({ ...validateError, [field]: "" });
        return true
      }
    }
    if (field == 'name') {
      if (value.length > 64) {
        setValidateError({ ...validateError, [field]: "Too long" });
        return false
      } else if (value.trim() == '') {
        setValidateError({ ...validateError, [field]: "Must be not empty" });
        return false
      } else {
        setValidateError({ ...validateError, [field]: "" });
        return true
      }
    }
    if (field == 'color' || field == 'type') {
      if (value.trim() == '') {
        setValidateError({ ...validateError, [field]: "Must be not empty" });
        return false
      } else {
        setValidateError({ ...validateError, [field]: "" });
        return true
      }
    }
    return true
  }

  const validateNestedFiled = (parentField, field, value) => {
    console.log("Validate:", parentField, field, value)
    if (parentField == 'killer') {
      if (field == 'weight') {
        if (isNaN(value) || !(/^\+?([1-9]\d*)?$/.test(value))) {
          setValidateError({ ...validateError, [parentField]: { ...validateError[parentField], [field]: "Invalid integer" } });
          return false
        } else {
          setValidateError({ ...validateError, [parentField]: { ...validateError[parentField], [field]: "" } });
          return true
        }
      }
      if (field == 'name') {
        if (value.length > 64) {
          setValidateError({ ...validateError, [parentField]: { ...validateError[parentField], [field]: "Too long" } });
          return false
        } else {
          setValidateError({ ...validateError, [parentField]: { ...validateError[parentField], [field]: "" } });
          return true
        }
      }
    }
    if (parentField == 'coordinates') {
      if (isNaN(value) || isNaN(parseFloat(value))) {
        setValidateError({ ...validateError, [parentField]: { ...validateError[parentField], [field]: "Invalid number" } });
        return false
      } else {
        setValidateError({ ...validateError, [parentField]: { ...validateError[parentField], [field]: "" } });
        return true
      }
    }
    return true
  }

  const handleChange = (field, value) => {
    validateFiled(field, value)
    setDragon({ ...dragon, [field]: value });
  };

  const handleNestedChange = (parentField, field, value) => {
    validateNestedFiled(parentField, field, value)
    setDragon({ ...dragon, [parentField]: { ...dragon[parentField], [field]: value } });
  };

  const handleSubmit = async () => {    
    var isValid = true
    for (let [key, value] of Object.entries(dragon)) {
      if (key == 'coordinates' || key == 'killer') {
        for (let [nestKey, nestValue] of Object.entries(value)) {
          if (!validateNestedFiled(key, nestKey, nestValue)) {
            isValid = false
            break
          }
        }
      } else {
        if (!validateFiled(key, value)) {
          isValid = false
        }
      }
      if (!isValid) 
        break
    }
    if (!isValid) {
      console.log("Not valid")
      return
    }

    var dto = null
    if (dragon.killer.name == '' && dragon.killer.weight == '' && dragon.killer.eyeColor == '' && dragon.killer.nationality == '') {
      dto =  { ...dragon, killer: null }
    } else dto = dragon

    if (dragon.character == '')
      dto =  { ...dto, character: null }

    console.log("Send request:")
    console.log(dto)

    var response = null
    if (dragonToEdit) {
      response = await makeApiRequest(updateDragon, dragonToEdit.id, dto);
    } else {
      response = await makeApiRequest(createDragon, dto);
    }
    if (response != null) {
      onClose();
    }
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{dragonToEdit ? 'Edit Dragon' : 'Create New Dragon'}</DialogTitle>
      <DialogContent>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              required
              label="Name"
              value={dragon.name}
              onChange={(e) => handleChange('name', e.target.value)}
              error={validateError.name != '' ? true : false}
              helperText={validateError.name}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              required
              label="Coordinates X"
              value={dragon.coordinates.x}
              onChange={(e) => handleNestedChange('coordinates', 'x', e.target.value)}
              error={validateError.coordinates.x != '' ? true : false}
              helperText={validateError.coordinates.x}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              required
              label="Coordinates Y"
              value={dragon.coordinates.y}
              onChange={(e) => handleNestedChange('coordinates', 'y', e.target.value)}
              error={validateError.coordinates.y != '' ? true : false}
              helperText={validateError.coordinates.y}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              required
              label="Age"
              value={dragon.age}
              onChange={(e) => handleChange('age', e.target.value)}
              error={validateError.age != '' ? true : false}
              helperText={validateError.age}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <Select
              required
              value={dragon.color}
              onChange={(e) => handleChange('color', e.target.value)}
              error={validateError.color != '' ? true : false}
              helperText={validateError.color}
              displayEmpty
              fullWidth
            >
              <MenuItem value="" disabled>Color</MenuItem>
              {Colors.map(item => (
                <MenuItem key={item} value={item}>
                  {toTitleCase(item)}
                </MenuItem>
              ))}
            </Select>
          </Grid>
          <Grid item xs={6}>
            <Select
              required
              value={dragon.type}
              onChange={(e) => handleChange('type', e.target.value)}
              error={validateError.type != '' ? true : false}
              helperText={validateError.type}
              displayEmpty
              fullWidth
            >
              <MenuItem value="" disabled>Type</MenuItem>
              {DragonTypes.map(item => (
                <MenuItem key={item} value={item}>
                  {toTitleCase(item)}
                </MenuItem>
              ))}
            </Select>
          </Grid>
          <Grid item xs={6}>
            <Select
              value={dragon.character}
              onChange={(e) => handleChange('character', e.target.value)}
              error={validateError.character != '' ? true : false}
              helperText={validateError.character}
              displayEmpty
              fullWidth
            >
              <MenuItem value="">Character</MenuItem>
              {DragonCharacters.map(item => (
                <MenuItem key={item} value={item}>
                  {toTitleCase(item)}
                </MenuItem>
              ))}
            </Select>
          </Grid>
          <Grid item xs={12}>
            <TextField
              label="Killer Name"
              value={dragon.killer != null ? dragon.killer.name : ''}
              onChange={(e) => handleNestedChange('killer', 'name', e.target.value)}
              error={validateError.killer.name != '' ? true : false}
              helperText={validateError.killer.name}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              label="Killer Weight"
              value={dragon.killer != null ? dragon.killer.weight : ''}
              onChange={(e) => handleNestedChange('killer', 'weight', e.target.value)}
              error={validateError.killer.weight != '' ? true : false}
              helperText={validateError.killer.weight}
              fullWidth
            />
          </Grid>
          <Grid item xs={6}>
            <Select
              value={dragon.killer != null ? dragon.killer.eyeColor : ''}
              onChange={(e) => handleNestedChange('killer', 'eyeColor', e.target.value)}
              error={validateError.killer.eyeColor != '' ? true : false}
              helperText={validateError.killer.eyeColor}
              displayEmpty
              fullWidth
            >
              <MenuItem value="">Killer Eye Color</MenuItem>
              {Colors.map(item => (
                <MenuItem key={item} value={item}>
                  {toTitleCase(item)}
                </MenuItem>
              ))}
            </Select>
          </Grid>
          <Grid item xs={6}>
            <Select
              value={dragon.killer != null ? dragon.killer.nationality : ''}
              onChange={(e) => handleNestedChange('killer', 'nationality', e.target.value)}
              error={validateError.killer.nationality != '' ? true : false}
              helperText={validateError.killer.nationality}
              displayEmpty
              fullWidth
            >
              <MenuItem value="">Killer Nationality</MenuItem>
              {Countries.map(item => (
                <MenuItem key={item} value={item}>
                  {toTitleCase(item)}
                </MenuItem>
              ))}
            </Select>
          </Grid>
          <Grid item xs={6}>
            <TextField
              required
              label="Cave ID"
              value={dragon.cave ? dragon.cave.id : null}
              onChange={(e) => handleChange('caveId', e.target.value)}
              error={validateError.caveId != '' ? true : false}
              helperText={validateError.caveId}
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
          {dragonToEdit ? 'Update Dragon' : 'Create Dragon'}
        </Button>
      </DialogContent>
    </Dialog>
  );
};

export default DragonForm;
