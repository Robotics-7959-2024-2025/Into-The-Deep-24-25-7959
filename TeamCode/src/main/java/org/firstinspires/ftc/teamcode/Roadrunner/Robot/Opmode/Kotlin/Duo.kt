package org.firstinspires.ftc.teamcode.Roadrunner.Robot.Opmode.Kotlin

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Roadrunner.Auto.References.Pipelines.ItWasAllRedAndYellow
import org.firstinspires.ftc.teamcode.Roadrunner.Robot.Opmode.Kotlin.MotorsKT
import kotlin.math.pow
import kotlin.math.sign

@TeleOp(name = "Teleop 7959 Duo KT")
class DuoKT : LinearOpMode() {

    private val ctrlPow = 2.0
    private val yTime = 0.3
    private val gTime = 0.7
    private var rx = 0.0; var R = 0.0; var desiredAngle = 0.0; var RR = 0.0
    private val time = ElapsedTime()
    private var intakeToggle = false; var outToggle = false; var moveOutWrist = false
    private var readyToOutGrip = false; var gripBackDown = false; var eviltog = false


    override fun runOpMode() {
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        MotorsKT.init(hardwareMap)
        MotorsKT.runWithoutEncoder()

        val currentGamepad1 = Gamepad()
        val previousGamepad1 = Gamepad()
        val outPrev = Gamepad()
        val outNow = Gamepad()

        MotorsKT.servos["outL"]?.position = 0.6
        MotorsKT.servos["outR"]?.position = 0.4
        MotorsKT.servos["outWrist"]?.position = 0.0
        MotorsKT.servos["outClaw"]?.position = 0.4
        eviltog = true
        outToggle = true

        waitForStart()

        MotorsKT.motors["liftL"]?.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        MotorsKT.motors["liftL"]?.mode = DcMotor.RunMode.RUN_USING_ENCODER
        MotorsKT.motors["liftL"]?.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        MotorsKT.servos["elbowR"]?.position = 0.67
        MotorsKT.servos["elbowL"]?.position = 0.33
        val time = ElapsedTime()
        time.reset()
        if (isStopRequested) return
        while (opModeIsActive()) {
            telemetry.addData("Time:", time.toString())
            telemetry.addData("Extendo", MotorsKT.motors["extendo"]?.currentPosition)
            telemetry.addData("Lift", MotorsKT.motors["liftL"]?.currentPosition)
            telemetry.addData("Right Axon pos:", MotorsKT.servos["wristR"]?.position)
            telemetry.addData("Left Axon pos:", MotorsKT.servos["wristL"]?.position)
            telemetry.update()

            MotorsKT.motors["liftL"]?.power = when {
                gamepad2.left_trigger > 0 -> -gamepad2.left_trigger.toDouble()
                gamepad2.right_trigger > 0 -> gamepad2.right_trigger.toDouble()
                //feedfowad component
                else -> 0.0005
            }

            outPrev.copy(outNow)
            outNow.copy(gamepad2)
            previousGamepad1.copy(currentGamepad1)
            currentGamepad1.copy(gamepad1)

            rx = (gamepad1.right_stick_x / 4) * 3.0


            MotorsKT.motors["rightFront"]?.power = (gamepad1.left_stick_y + gamepad1.left_stick_x + rx).pow(ctrlPow) *
                    sign(gamepad1.left_stick_y + gamepad1.left_stick_x + rx)
            MotorsKT.motors["leftFront"]?.power = (gamepad1.left_stick_y - gamepad1.left_stick_x - rx).pow(ctrlPow) *
                    sign(gamepad1.left_stick_y - gamepad1.left_stick_x - rx)
            MotorsKT.motors["rightBack"]?.power = (gamepad1.left_stick_y - gamepad1.left_stick_x + rx).pow(ctrlPow) *
                    sign(gamepad1.left_stick_y - gamepad1.left_stick_x + rx)
            MotorsKT.motors["leftBack"]?.power = (gamepad1.left_stick_y + gamepad1.left_stick_x - rx).pow(ctrlPow) *
                    sign(gamepad1.left_stick_y + gamepad1.left_stick_x - rx)

            MotorsKT.motors["extendo"]?.power = -gamepad1.right_stick_y.toDouble()


            if(gamepad1.left_bumper){
                //0.7882
                MotorsKT.servos["wristR"]?.position = (1-((135)/(255*1.6)));
                //0.2118
                MotorsKT.servos["wristL"]?.position = (1-((135)/(255*1.6)));
            }

            if(gamepad1.a)
            {
                //180/300 is 0.6
//                wristL.setPosition(((135.0)/(255.0*(1.6))));
//                wristR.setPosition(1.0-((135.0)/(255.0*(1.6))));
                MotorsKT.servos["elbowR"]?.position = (0.98);
                MotorsKT.servos["elbowL"]?.position = (0.02);
                MotorsKT.servos["claw"]?.position = (0.0);
                    intakeToggle = false;
            }


            //moves to pre-grabbing position
            if(gamepad2.dpad_left){
                MotorsKT.servos["outWrist"]?.position = (0.6);

            }

            //moves to grabbing position
            if(gamepad2.dpad_right){
                MotorsKT.servos["outWrist"]?.position = (0.0);
            }
            if(gamepad2.dpad_down){
                MotorsKT.servos["outL"]?.position = ((0.5));
                MotorsKT.servos["outR"]?.position =(0.5);
            }


            if(gamepad2.dpad_up){
                MotorsKT.servos["outL"]?.position = ((1.0));
                MotorsKT.servos["outR"]?.position = (0.0);
            }

            if(gamepad1.right_bumper) {
                //0.1098
                R = 70 / (255 * 1.6);
                //0.8980
                MotorsKT.servos["wristR"]?.position = (1 - ((135) / (255 * 1.6)) + R);
                //0.3216
                MotorsKT.servos["wristL"]?.position = (((135) / (255 * 1.6)) + R);
            }



            if (gamepad2.right_bumper) {
                intakeToggle = false
                MotorsKT.servos["claw"]?.position = 0.0
                MotorsKT.servos["elbowR"]?.position = 0.68
                MotorsKT.servos["elbowL"]?.position = 0.32
                MotorsKT.servos["outL"]?.position = 0.6
                MotorsKT.servos["outR"]?.position = 0.4
                MotorsKT.servos["outWrist"]?.position = 0.0
            }

            if (gamepad2.left_bumper) {
                //put this jawn down
                MotorsKT.servos["outL"]?.position = 0.0
                MotorsKT.servos["outR"]?.position = 1.0
                //opens the claw
                outToggle = false;
                MotorsKT.servos["outClaw"]?.position = 0.0


                //wrist.setPosition(0.9);
                MotorsKT.servos["elbowR"]?.position = 0.8
                MotorsKT.servos["elbowL"]?.position = 0.2
                MotorsKT.servos["claw"]?.position = 0.0
                intakeToggle = false;

                telemetry.addData("current pos", MotorsKT.servos["outClaw"]?.position);
                telemetry.update();
            }

            if (gamepad2.y) {
                MotorsKT.servos["outL"]?.position = 0.17
                MotorsKT.servos["outR"]?.position = 1 - 0.17
                MotorsKT.servos["outWrist"]?.position = 0.18
                MotorsKT.servos["elbowR"]?.position = 0.35
                MotorsKT.servos["elbowL"]?.position = 1 - 0.35
                MotorsKT.servos["wristL"]?.position = 135 / (255 * 1.6)
                MotorsKT.servos["wristR"]?.position = 1 - (135 / (255 * 1.6))
                outToggle = false
                MotorsKT.servos["outClaw"]?.position = 0.0
            }

            if(gamepad1.b){
                MotorsKT.servos["elbowR"]?.position = 0.8
                MotorsKT.servos["elbowL"]?.position = 0.2
            }

            if (gamepad2.b) {
                time.reset()
                MotorsKT.servos["elbowR"]?.position = 0.8
                MotorsKT.servos["elbowL"]?.position = 0.2
                if (time.seconds() > 0.4) {
                    ItWasAllRedAndYellow.getAngle()
                }

                desiredAngle = ItWasAllRedAndYellow.getAngle() * 0.777778
                if (ItWasAllRedAndYellow.getAngle() > 90) {
                    desiredAngle = (ItWasAllRedAndYellow.getAngle() - 90) * 0.777778
                }
                RR = desiredAngle / (255 * 2.5)
                MotorsKT.servos["wristR"]?.position = 1 - (135 / (255 * 2.5)) + R
                MotorsKT.servos["wristL"]?.position = (135 / (255 * 2.5)) + R
            }

            if (gamepad1.y) {
                time.reset()
                telemetry.addData("time:", time.toString())
                telemetry.update()
                MotorsKT.servos["claw"]?.position = 0.75
                MotorsKT.servos["outWrist"]?.position = 0.3
                MotorsKT.servos["wristR"]?.position = 1.0
                MotorsKT.servos["wristL"]?.position = 0.0
                MotorsKT.servos["elbowR"]?.position = 0.66
                MotorsKT.servos["elbowL"]?.position = 0.34
                outToggle = false
                MotorsKT.servos["outClaw"]?.position = 0.0
                moveOutWrist = true
            }

            if (moveOutWrist && time.seconds() >= yTime) {
                MotorsKT.servos["claw"]?.position = 0.24
                MotorsKT.servos["outWrist"]?.position = 0.0
                moveOutWrist = false
                outToggle = false
                time.reset()
                readyToOutGrip = true
            }

            if (readyToOutGrip && time.seconds() >= gTime) {
                MotorsKT.servos["outClaw"]?.position = 0.35
                outToggle = true
                readyToOutGrip = false
                time.reset()
                gripBackDown = true
            }
            if (gamepad1.b) {
                MotorsKT.servos["elbowR"]?.position = 0.8
                MotorsKT.servos["elbowL"]?.position = 0.2
            }

            if (currentGamepad1.x && !previousGamepad1.x) {
                intakeToggle = !intakeToggle;
            }
            if (outNow.x && !outPrev.x) {
                outToggle = !outToggle;
            }
            if (outNow.a && !outPrev.a) {
                eviltog = !eviltog;
            }

            if (gamepad1.x) {
                MotorsKT.servos["claw"]?.position = if (intakeToggle) 0.375 else 0.0
            }

            MotorsKT.servos["outClaw"]?.position = if (outToggle) 0.35 else 0.0
        }
    }
}
