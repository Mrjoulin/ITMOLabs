import React, { useEffect, useState } from 'react';
import { getDragons, deleteDragon, deleteAllDragonsByType } from '../../services/dragonService';
import { DragonTypes, DragonCharacters, Countries, Colors } from  './EnumConsts';
import { Box, TextField, Button, Table, TableHead, TableRow, TableCell, TableBody, IconButton, Container, Select, MenuItem } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import CloseIcon from '@mui/icons-material/Close';


const SORT_FIELDS = [
  'id', 'name', 'creationDate', 'coordinates.x', 'coordinates.y', 'age', 'color', 'type', 'character', 
  'killer.name', 'killer.weight', 'killer.eyeColor', 'killer.nationality', 'caveId'
];

const INT_FIELDS = [
  'id', 'age', 'killer.weight', 'caveId'
]

const NUMBER_FIELDS = [
  'id', 'coordinates.x', 'coordinates.y', 'age', 'killer.weight', 'caveId'
]
const ENUM_FIELDS = {
  'color': Colors, 'type': DragonTypes, 'character': DragonCharacters, 'killer.eyeColor': Colors, 'killer.nationality': Countries
}

const FILTER_SIGNS = ['=', '!=', '>', '<', '>=', '<='];
const STRING_FILTER_SIGNS = ['=', '!='];

function toTitleCase(str) {
  return str.replace('_', ' ').replace(
    /\w\S*/g,
    text => text.charAt(0).toUpperCase() + text.substring(1).toLowerCase()
  );
}


const DragonTable = ({ onEdit, makeApiRequest, fetchDragonsFlag }) => {
  const [dragons, setDragons] = useState([]);
  const [filters, setFilters] = useState({ sort: [], filter: [], page: 0, pageSize: 10 });
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize, setPageSize] = useState(10)
  const [filtersSigns, setFiltersSigns] = useState([])
  const [filtersErrors, setFiltersErrors] = useState([])

  useEffect(() => {
    fetchDragons();
  }, [filters, fetchDragonsFlag]);

  const fetchDragons = async () => {
    console.log(filters)
    const response = await makeApiRequest(getDragons, filters);
    if (response != null) {
      setDragons(response.data.results);
      setTotalPages(response.data.numPages);
    }
    fetchDragonsFlag = false;
  };

  const handleDelete = async (dragonId) => {
    await makeApiRequest(deleteDragon, dragonId);
    fetchDragons();
  };

  const handleDeleteByType = async (type) => {
    await makeApiRequest(deleteAllDragonsByType, type);
    fetchDragons();
  };

  const handleEdit = (dragon) => {
    onEdit(dragon);  // Pass dragon data to edit form
    fetchDragons();
  };

  const addSortOption = () => {
    setFilters(prev => ({ ...prev, sort: [...prev.sort, ''] }));
  };

  const addFilterOption = () => {
    setFilters(prev => ({
      ...prev,
      filter: [...prev.filter, { field: '', sign: '=', target: '' }]
    }));
    setFiltersSigns(prev => [...prev, FILTER_SIGNS])
    setFiltersErrors(prev => [...prev, ''])
  };

  const updateSortField = (index, value) => {
    const newSort = [...filters.sort];
    newSort[index] = value;
    setFilters(prev => ({ ...prev, sort: newSort }));
  };

  const updateFilterField = (index, field, value) => {
    const newFilter = [...filters.filter];
    newFilter[index] = { ...newFilter[index], [field]: value };
    setFilters(prev => ({ ...prev, filter: newFilter }));
  
    if (field == 'field') {
      const newFiltersSigns = [...filtersSigns]
      if (NUMBER_FIELDS.includes(value) || INT_FIELDS.includes(value) || value == 'creationDate') {
        newFiltersSigns[index] = FILTER_SIGNS
      } else {
        newFiltersSigns[index] = STRING_FILTER_SIGNS
      }
      setFiltersSigns(newFiltersSigns)
    } else if (field == 'target'){
      const newFilterErrors = [...filtersErrors]
      if (INT_FIELDS.includes(newFilter[index]['field'])) {
        if (isNaN(value) || !(/^\+?([1-9]\d*)?$/.test(value))) {
          newFilterErrors[index] = 'Invalid integer'
        } else {
          newFilterErrors[index] = ''
        }
      } else if (NUMBER_FIELDS.includes(newFilter[index]['field'])) { 
        if (isNaN(value) || isNaN(parseFloat(value))) {
          newFilterErrors[index] = 'Invalid number'
        } else if (value.split('.').length > 1 && value.split('.')[1].length > 15) {
          newFilterErrors[index] = 'Too high precision, max 15 digits'
        } else {
          newFilterErrors[index] = ''
        }
      }
      setFiltersErrors(newFilterErrors)
    }
  };

  const deleteSortOption = (index) => {
    const newSort = filters.sort.filter((_, i) => i !== index);
    setFilters(prev => ({ ...prev, sort: newSort }));
  };

  const deleteFilterOption = (index) => {
    const newFilter = filters.filter.filter((_, i) => i !== index);
    setFilters(prev => ({ ...prev, filter: newFilter }));
  };

  const handlePageSizeChange = (value) => {
    if (/^\+?([1-9]\d*)?$/.test(value)) {
      setPageSize(value != '' ? parseInt(value) : '')
    }
  }

  const validatePageSize = (value) => {
    if (!(/^\+?([1-9]\d*)$/.test(value))) {
      setPageSize(10)
      setFilters({ ...filters, pageSize: 10 })
    } else if (parseInt(value) < 10) {
      setPageSize(10)
      setFilters({ ...filters, pageSize: 10 })
    } else if (parseInt(value) > 100) {
      setPageSize(100)
      setFilters({ ...filters, pageSize: 100 })
    } else {
      setFilters({ ...filters, pageSize: parseInt(value) })
    }
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center'}}>
      <Box sx={{ mb: 2, display: 'flex', flexDirection: 'row', alignContent: 'center', alignItems: 'center', textAlign: 'center'}}>
        <div>
          <h3>Sort Options</h3>
          {filters.sort.map((sortField, index) => (
            <Box key={index} sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Select
                value={sortField}
                onChange={(e) => updateSortField(index, e.target.value)}
                displayEmpty
                sx={{ mr: 2, minWidth: 200 }}
              >
                <MenuItem value="" disabled>Select field</MenuItem>
                {SORT_FIELDS.map(field => (
                  <MenuItem key={field} value={field}>
                    {field}
                  </MenuItem>
                ))}
                {SORT_FIELDS.map(field => (
                  <MenuItem key={`-${field}`} value={`-${field}`}>
                    {`${field} (desc)`}
                  </MenuItem>
                ))}
              </Select>
              <IconButton onClick={() => deleteSortOption(index)}>
                <CloseIcon />
              </IconButton>
            </Box>
          ))}
          <Button onClick={addSortOption}>+ Add Sort Option</Button>
        </div>
        <div>
          <h3>Filter Options</h3>
            {filters.filter.map((filter, index) => (
            <Box key={index} sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Select
                value={filter.field}
                onChange={(e) => updateFilterField(index, 'field', e.target.value)}
                displayEmpty
                sx={{ mr: 2, minWidth: 200 }}
              >
                <MenuItem value="" disabled>Select field</MenuItem>
                {SORT_FIELDS.map(field => (
                  <MenuItem key={field} value={field}>
                    {field}
                  </MenuItem>
                ))}
              </Select>
              <Select
                value={filter.sign}
                disabled={filter.field == ''}
                onChange={(e) => updateFilterField(index, 'sign', e.target.value)}
                displayEmpty
                sx={{ mr: 2, minWidth: 100 }}
              >
                {filtersSigns[index].map(sign => (
                  <MenuItem key={sign} value={sign}>
                    {sign}
                  </MenuItem>
                ))}
              </Select>
              {(() => {
                if (ENUM_FIELDS.hasOwnProperty(filter.field)) {
                  return (
                    <Select
                      value={filter.target}
                      onChange={(e) => updateFilterField(index, 'target', e.target.value)}
                      displayEmpty
                      sx={{ mr: 2, minWidth: 200 }}
                    >
                      <MenuItem value="" disabled>Select item</MenuItem>
                      {ENUM_FIELDS[filter.field].map(item => (
                        <MenuItem key={item} value={item}>
                          {toTitleCase(item)}
                        </MenuItem>
                      ))}
                    </Select>
                  )
                } else {
                  return (
                    <TextField
                      value={filter.target}
                      disabled={filter.field == ''}
                      onChange={(e) => updateFilterField(index, 'target', e.target.value)}
                      error={filtersErrors[index] != ''}
                      helperText={filtersErrors[index]}
                      label="Value"
                      sx={{ mr: 2, minWidth: 200 }}
                    />
                  )
                }
              })()}              
              <IconButton onClick={() => deleteFilterOption(index)}>
                <CloseIcon />
              </IconButton>
            </Box>
          ))}
          <Button onClick={addFilterOption}>+ Add Filter Option</Button>
        </div>
        <Box sx={{ display: 'flex', flexDirection: 'column', alignContent: 'center', textAlign: 'center'}}>
          <Button onClick={() => handleDeleteByType('FIRE')}>Delete All Fire Dragons</Button>
          <Button onClick={() => handleDeleteByType('WATER')}>Delete All Water Dragons</Button>
          <Button onClick={() => handleDeleteByType('UNDERGROUND')}>Delete All Underground Dragons</Button>
        </Box>
      </Box>

      <Box sx={{ mb: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Created</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Coordinates</TableCell>
              <TableCell>Age</TableCell>
              <TableCell>Color</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Character</TableCell>
              <TableCell>Killer</TableCell>
              <TableCell>In cave</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {dragons.map((dragon) => (
              <TableRow key={dragon.id}>
                <TableCell>{dragon.id}</TableCell>
                <TableCell>{dragon.creationDate}</TableCell>
                <TableCell>{dragon.name}</TableCell>
                <TableCell>{`(${dragon.coordinates.x}, ${dragon.coordinates.y})`}</TableCell>
                <TableCell>{dragon.age}</TableCell>
                <TableCell>{dragon.color}</TableCell>
                <TableCell>{dragon.type}</TableCell>
                <TableCell>{dragon.character}</TableCell>
                <TableCell>{dragon.killer ? dragon.killer.name : null}</TableCell>
                <TableCell>{dragon.cave ? dragon.cave.id : null}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleEdit(dragon)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton onClick={() => handleDelete(dragon.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Box>

      {/* Pagination controls */}
      <Container maxWidth="lg" sx={{ display: 'flex',  flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', textAlign: 'right', mr: 2}}>
        <TextField
          value={pageSize}
          onChange={(e) => handlePageSizeChange(e.target.value)}
          onBlur={(e) => validatePageSize(e.target.value)}
          label="Page Size"
          sx={{ mr: 2, minWidth: 200 }}
        />
        <div>
          {Array.from({ length: totalPages }, (_, index) => (
            <Button
              key={index}
              onClick={() => setFilters({ ...filters, page: index })}
              variant={filters.page === index ? 'contained' : 'outlined'}
            >
              {index + 1}
            </Button>
          ))}
        </div>
      </Container>
    </Box>
  );
};

export default DragonTable;