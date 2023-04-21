package hu.bme.mit.train.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainUser;

import static org.mockito.Mockito.*;

public class TrainSensorTest {
    TrainController mockTrainController;
    TrainUser mockTrainUser;
    TrainSensorImpl trainSensor;

    @Before
    public void before() {
        mockTrainController = mock(TrainController.class);
        mockTrainUser = mock(TrainUser.class);
        trainSensor = new TrainSensorImpl(mockTrainController, mockTrainUser);

        when(mockTrainController.getReferenceSpeed()).thenReturn(100);

        trainSensor.overrideSpeedLimit(100);
        mockTrainUser.setAlarmState(false);
    }

    /**
     * Prerequisites: Speedlimit in valid range, alarmstate flag is false.
     * Input: Set the new speedlimit to a negative value.
     * Expected output: The alarmstate flag is true.
     */
    @Test
    public void TestNegativeSpeedLimit() {
        trainSensor.overrideSpeedLimit(-1);

        verify(mockTrainUser, times(1)).setAlarmState(true);
        verify(mockTrainController, times(0)).setSpeedLimit(-1);
    }

    /**
     * Prerequisites: Speedlimit in valid range, alarmstate flag is false.
     * Input: Set the new speedlimit to a value outside the correct range.
     * Expected output: The old speedlimit is kept.
     */
    @Test
    public void TestSpeedLimitNotSetOnInvalidNewValue() {
        trainSensor.overrideSpeedLimit(690);

        verify(mockTrainUser, times(1)).setAlarmState(true);
        verify(mockTrainController, times(0)).setSpeedLimit(690);
    }

    /**
     * Prerequisites: Speedlimit in valid range, alarmstate flag is false.
     * Input: Set the new speedlimit to a value tat is less than half of the current
     * speedlimit.
     * Expected output: The alarmstate flag is true.
     */
    @Test
    public void TestSpeedLimitTooSmall() {
        trainSensor.overrideSpeedLimit(42);

        verify(mockTrainUser, times(1)).setAlarmState(true);
        verify(mockTrainController, times(0)).setSpeedLimit(42);
    }

    /**
     * Prerequisites: Speedlimit in valid range, alarmstate flag is false.
     * Input: Set the new speedlimit to a value between 0 and 500.
     * Expected output: The alarmstate flag is false and the new speedlimit is
     * set.
     */
    @Test
    public void TestSpeedLimitInCorrectRange() {
        trainSensor.overrideSpeedLimit(69);

        verify(mockTrainUser, times(0)).setAlarmState(true);
        verify(mockTrainController, times(1)).setSpeedLimit(69);
    }

    /**
     * Prerequisites: The speedlimit is set to 100
     * Input: Call the getSpeedLimit() getter.
     * Expected output: The value of the speedLimit is returned.
     */
    @Test
    public void TestGetSpeedLimit() {
        Assert.assertEquals(100, trainSensor.getSpeedLimit());
    }

}
