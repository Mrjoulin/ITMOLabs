package task3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class DomainModelTest {
    @Test
    fun testStartBombing() {
        val weather = Weather()
        val bombing = Bombing(weather)

        assertFalse(bombing.isBombing)

        bombing.startBombing()

        assertTrue(bombing.isBombing)
        assertEquals(Level.UNIMAGINABLE, weather.temperatureLevel)
        assertEquals(Level.UNIMAGINABLE, weather.noiseLevel)

        bombing.startBombing()
        assertTrue(bombing.isBombing)
    }

    @Test
    fun testEndBombing() {
        val weather = Weather()
        val bombing = Bombing(weather)

        assertFalse(bombing.isBombing)

        bombing.endBombing()
        assertFalse(bombing.isBombing)

        bombing.startBombing()
        assertTrue(bombing.isBombing)

        bombing.endBombing()
        assertFalse(bombing.isBombing)
        assertEquals(Level.HIGH, weather.temperatureLevel)
        assertEquals(Level.NORMAL, weather.noiseLevel)
    }

    @Test
    fun testComputerBankStateDestroy() {
        val state = ComputerBankState("Банк", false)

        assertFalse(state.isBroken)

        state.destroy()
        assertTrue(state.isBroken)

        state.destroy()
        assertTrue(state.isBroken)
    }

    @Test
    fun testComputerBankStateRepair() {
        val state = ComputerBankState("Банк", false)

        assertFalse(state.isBroken)

        state.repair()
        assertFalse(state.isBroken)

        state.destroy()
        assertTrue(state.isBroken)

        state.repair()
        assertFalse(state.isBroken)
    }

    @Test
    fun testComputerBankPieceMelt() {
        val piece1 = ComputerBankPiece("Test1", false)

        assertFalse(piece1.isMelted)
        piece1.melt()
        assertFalse(piece1.isMelted)

        val piece2 = ComputerBankPiece("Test2", true)

        assertFalse(piece2.isMelted)

        piece2.melt()
        assertTrue(piece2.isMelted)

        piece2.melt()
        assertTrue(piece2.isMelted)
    }

    @Test
    fun testComputerBankDefaultTemperature() {
        val weather = Weather()
        val computerBank = ComputerBank("Банк", weather)

        assertFalse(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }

        computerBank.checkState()

        assertFalse(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
    }

    @Test
    fun testComputerBankHighTemperature() {
        val weather = Weather()
        val computerBank = ComputerBank("Банк", weather)

        computerBank.checkState()

        assertFalse(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }

        weather.temperatureLevel = Level.HIGH

        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
    }

    @Test
    fun testComputerBankUnimaginableTemperature() {
        val weather = Weather()
        val computerBank = ComputerBank("Банк", weather)

        computerBank.checkState()

        assertFalse(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }

        weather.temperatureLevel = Level.UNIMAGINABLE

        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        if (computerBank.pieces.any { it.isMetal })
            assertTrue(computerBank.isSomethingMetalMelting())
        else
            assertFalse(computerBank.isSomethingMetalMelting())
    }

    @Test
    fun testComputerBankTemperatureDowngrade() {
        val weather = Weather()
        val computerBank = ComputerBank("Банк", weather)

        weather.temperatureLevel = Level.UNIMAGINABLE
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        if (computerBank.pieces.any { it.isMetal })
            assertTrue(computerBank.isSomethingMetalMelting())
        else
            assertFalse(computerBank.isSomethingMetalMelting())

        weather.temperatureLevel = Level.HIGH
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        if (computerBank.pieces.any { it.isMetal })
            assertTrue(computerBank.isSomethingMetalMelting())
        else
            assertFalse(computerBank.isSomethingMetalMelting())

        weather.temperatureLevel = Level.NORMAL
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        if (computerBank.pieces.any { it.isMetal })
            assertTrue(computerBank.isSomethingMetalMelting())
        else
            assertFalse(computerBank.isSomethingMetalMelting())
    }

    @Test
    fun testComputerBankTemperatureUpgrade() {
        val weather = Weather()
        val computerBank = ComputerBank("Банк", weather)

        weather.temperatureLevel = Level.NORMAL
        computerBank.checkState()

        assertFalse(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
        assertFalse(computerBank.isSomethingMetalMelting())

        weather.temperatureLevel = Level.HIGH
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
        assertFalse(computerBank.isSomethingMetalMelting())

        weather.temperatureLevel = Level.UNIMAGINABLE
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        if (computerBank.pieces.any { it.isMetal })
            assertTrue(computerBank.isSomethingMetalMelting())
        else
            assertFalse(computerBank.isSomethingMetalMelting())
    }

    @Test
    fun testComputerBankDangerLevel() {
        val weather = Weather()
        val computerBank = ComputerBank("Банк", weather)

        computerBank.checkState()

        assertFalse(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
        assertFalse(computerBank.isSomethingMetalMelting())

        computerBank.dangerLevel = Level.HIGH
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
        assertFalse(computerBank.isSomethingMetalMelting())

        weather.temperatureLevel = Level.HIGH
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        computerBank.pieces.forEach { assertFalse(it.isMelted) }
        assertFalse(computerBank.isSomethingMetalMelting())

        weather.temperatureLevel = Level.UNIMAGINABLE
        computerBank.checkState()

        assertTrue(computerBank.state.isBroken)
        if (computerBank.pieces.any { it.isMetal })
            assertTrue(computerBank.isSomethingMetalMelting())
        else
            assertFalse(computerBank.isSomethingMetalMelting())
    }

    @Test
    fun testCoordinatesDist() {
        val cord1 = Coordinates(0, 0)
        val cord2 = Coordinates(1, 1)
        val cord3 = Coordinates(-1, -1)
        val cord4 = Coordinates(3, 4)

        assertEquals(sqrt(2.0), cord1.dist(cord2), 1e-6)
        assertEquals(cord1.dist(cord2), cord1.dist(cord3), 1e-6)
        assertEquals(2 * sqrt(2.0), cord2.dist(cord3),1e-6)

        assertEquals(0.0, cord1.dist(cord1), 1e-6)
        assertEquals(5.0, cord1.dist(cord4), 1e-6)
    }

    @Test
    fun testLocationsNearest() {
        val testLocObj = People("Котята")
        val locations = arrayListOf(
            Location("loc1", testLocObj, Coordinates(0, 0)),
            Location("loc2", testLocObj, Coordinates(1, 1)),
            Location("loc3", testLocObj, Coordinates(-1, -1)),
            Location("loc4", testLocObj, Coordinates(3, 4)),
        )

        assertEquals("loc2", locations[0].nearestLocation(locations)?.name)
        assertEquals("loc1", locations[1].nearestLocation(locations)?.name)
        assertEquals("loc1", locations[2].nearestLocation(locations)?.name)
        assertEquals("loc2", locations[3].nearestLocation(locations)?.name)
    }

    @Test
    fun testLocationsNearestNull() {
        val testLocObj = People("Котята")
        val location = Location("loc1", testLocObj, Coordinates(0, 0))

        assertNull(location.nearestLocation(arrayListOf()))
        assertNull(location.nearestLocation(arrayListOf(location)))
    }

    @Test
    fun testPeopleScaryHighDanger() {
        val people = People("Котята")

        people.checkState()
        assertFalse(people.scaryWaiting)

        people.dangerLevel = Level.HIGH

        people.checkState()
        assertTrue(people.scaryWaiting)
    }

    @Test
    fun testPeopleScaryUnimaginableDanger() {
        val people = People("Котята")

        people.checkState()
        assertFalse(people.scaryWaiting)

        people.dangerLevel = Level.UNIMAGINABLE

        people.checkState()
        assertTrue(people.scaryWaiting)
    }

    @Test
    fun testPeopleScaryDowngradeDanger() {
        val people = People("Котята")

        people.checkState()
        assertFalse(people.scaryWaiting)

        people.dangerLevel = Level.HIGH

        people.checkState()
        assertTrue(people.scaryWaiting)

        people.dangerLevel = Level.NORMAL

        people.checkState()
        assertFalse(people.scaryWaiting)
    }

    @Test
    fun testPlaceHighTemp() {
        val weather = Weather()
        val place = Place(weather)

        assertFalse(place.computerBank.state.isBroken)
        assertFalse(place.computerBank.isSomethingMetalMelting())
        place.locations.forEach { assertEquals(Level.NORMAL, it.locationObject.dangerLevel) }

        weather.temperatureLevel = Level.HIGH
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertFalse(place.computerBank.isSomethingMetalMelting())
        place.locations.forEach { assertEquals(Level.NORMAL, it.locationObject.dangerLevel) }
    }

    @Test
    fun testPlaceUnimaginableTemp() {
        val weather = Weather()
        val place = Place(weather)

        assertFalse(place.computerBank.state.isBroken)
        assertFalse(place.computerBank.isSomethingMetalMelting())
        place.locations.forEach { assertEquals(Level.NORMAL, it.locationObject.dangerLevel) }

        weather.temperatureLevel = Level.UNIMAGINABLE
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertTrue(place.computerBank.isSomethingMetalMelting())
        assertTrue(place.locations.any { it.locationObject.dangerLevel == Level.HIGH })
    }

    @Test
    fun testPlaceDowngrade() {
        val weather = Weather()
        val place = Place(weather)

        assertFalse(place.computerBank.state.isBroken)
        assertFalse(place.computerBank.isSomethingMetalMelting())
        place.locations.forEach { assertEquals(Level.NORMAL, it.locationObject.dangerLevel) }

        weather.temperatureLevel = Level.UNIMAGINABLE
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertTrue(place.computerBank.isSomethingMetalMelting())
        assertTrue(place.locations.any { it.locationObject.dangerLevel == Level.HIGH })

        weather.temperatureLevel = Level.HIGH
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertTrue(place.computerBank.isSomethingMetalMelting())
        assertTrue(place.locations.any { it.locationObject.dangerLevel == Level.HIGH })

        weather.temperatureLevel = Level.NORMAL
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertTrue(place.computerBank.isSomethingMetalMelting())
        assertTrue(place.locations.any { it.locationObject.dangerLevel == Level.HIGH })
    }

    @Test
    fun testPlaceTemperatureUp() {
        val weather = Weather()
        val place = Place(weather)

        assertFalse(place.computerBank.state.isBroken)
        assertFalse(place.computerBank.isSomethingMetalMelting())
        place.locations.forEach { assertEquals(Level.NORMAL, it.locationObject.dangerLevel) }

        weather.temperatureLevel = Level.HIGH
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertFalse(place.computerBank.isSomethingMetalMelting())
        place.locations.forEach { assertEquals(Level.NORMAL, it.locationObject.dangerLevel) }

        weather.temperatureLevel = Level.UNIMAGINABLE
        place.tick()

        assertTrue(place.computerBank.state.isBroken)
        assertTrue(place.computerBank.isSomethingMetalMelting())
        assertTrue(place.locations.any { it.locationObject.dangerLevel == Level.HIGH })
    }
}
