package org.firstinspires.ftc.teamcode.Roadrunner.Auto.Opmode.WorkingRR;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Roadrunner.Auto.Globals.Drive.PinpointDrive;
import org.firstinspires.ftc.teamcode.Roadrunner.Auto.References.RRActions.KeirsRRParts.*;

@Config
@Autonomous(name = "4 spec auto RR")
public class FourSpecAttempt extends LinearOpMode{


    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(7, -62, Math.toRadians(270));
        Pose2d wallPose = new Pose2d(46, -62, Math.toRadians(270));

        PinpointDrive drive = new PinpointDrive(hardwareMap, initialPose);
        Extendo extendo = new Extendo(hardwareMap, telemetry);
        Intake intake = new Intake(hardwareMap);
        Lift lift = new Lift(hardwareMap, telemetry);
        Outtake outtake = new Outtake(hardwareMap, telemetry);

        TrajectoryActionBuilder scorePreload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(11,-32), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));

        //preload scored
        TrajectoryActionBuilder pushSamples = drive.actionBuilder(new Pose2d(11,-32, Math.toRadians(270)))
                //.strafeTo(new Vector2d(11,-41))
                //.strafeTo(new Vector2d(33, -41))
                .splineToConstantHeading(new Vector2d(11, -40), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(32, -40), Math.toRadians(90))
                .lineToYConstantHeading(-20)
                .splineToConstantHeading(new Vector2d(46, -20), Math.toRadians(90))
                .lineToYConstantHeading(-45)
                //distance for the third spec
                .splineToConstantHeading(new Vector2d(46+10.5, -20), Math.toRadians(-30))
                .waitSeconds(0.001)
                .splineToConstantHeading(new Vector2d(46+10, -45), Math.toRadians(110))
                .splineToConstantHeading(new Vector2d(46+10, -43), Math.toRadians(110))
                .strafeTo(new Vector2d(46, -63), new TranslationalVelConstraint(80), new ProfileAccelConstraint(-35, 60));
        //spec 2 scored
        TrajectoryActionBuilder spec2scored = drive.actionBuilder(new Pose2d(46, -63, Math.toRadians(270)))
                .strafeTo(new Vector2d(-2, -32), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));
        //we are by the pit scoring the second

        //.splineToConstantHeading(new Vector2d(-2, -32), Math.toRadians(270), new TranslationalVelConstraint(60), new ProfileAccelConstraint(-35, 60));
        TrajectoryActionBuilder spec3 = drive.actionBuilder(new Pose2d(-2, -32, Math.toRadians(270)))
                //spec 3 picked up
                .strafeTo(new Vector2d(46, -63), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));
        TrajectoryActionBuilder spec3scored = drive.actionBuilder(wallPose)
                .strafeTo(new Vector2d(5, -32), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));

        TrajectoryActionBuilder spec4 = drive.actionBuilder(new Pose2d(5, -32, Math.toRadians(270)))
                //spec 3 picked up
                .strafeTo(new Vector2d(46, -63), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));
        TrajectoryActionBuilder spec4scored = drive.actionBuilder(wallPose)
                .strafeTo(new Vector2d(8, -32), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));

        TrajectoryActionBuilder parkSpline = drive.actionBuilder(new Pose2d(0, -32, Math.toRadians(270)))
                .strafeTo(new Vector2d(46, -55), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-35, 60));

        Actions.runBlocking(outtake.closeOutClaw());

        waitForStart();
        if (isStopRequested()) return;


        Action park = parkSpline.build();
        Action preload = scorePreload.build();
        Action push = pushSamples.build();
        Action score2 = spec2scored.build();
        Action take3 = spec3.build();
        Action score3 = spec3scored.build();
        Action take4 = spec4.build();
        Action score4 = spec4scored.build();

        Action IntakeDown = intake.IntakeDown();
        Action IntakeUp = intake.IntakeUp();
        Action OpenIntakeClaw = intake.openClaw();
        Action ICloseClaw = intake.closeClaw();

        //lift
        Action LiftUp = lift.liftUp();
        Action LiftDown = lift.liftDown();

        //outtake
        Action IntakeB = intake.IntakeB();
        Action OBoxDown = outtake.BoxDown();
        Action ODumpBox = outtake.DumpBox();
        Action OSpecBox = outtake.specDumpBox();
        Action CloseOutClaw = outtake.closeOutClaw();
        Action OpenOutClaw = outtake.openOutClaw();
        Action OutWristDown = outtake.OutWristDown();
        Action OutWristUp = outtake.OutWristUp();
        Action OutTuck = outtake.OuttakeTuck();
        Action Loosen = intake.Loosen();
        Action InTuck = intake.intakeTuck();
        Action SpecLiftUp = lift.specLiftUp();
        Action SpecLiftUp1 = lift.specLiftUp1();
        //extendo
        Action ExtendoOut = extendo.ExtendoOut();
        Action ExtendoIn = extendo.ExtendoIn();


        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                preload,
                                ODumpBox
                        ),
                        SpecLiftUp1,
                        new ParallelAction(
                                OpenOutClaw,
                                LiftDown,
                                push,
                                OutTuck
                        ),
                        CloseOutClaw,
                        new SleepAction(0.25),
                        new ParallelAction(
                                score2,
                                ODumpBox,
                                new SequentialAction(
                                        new SleepAction(1.75),
                                        SpecLiftUp
                                )
                        ),
                        new ParallelAction(
                                OpenOutClaw,
                                LiftDown,
                                OutTuck
                        ),
                        take3,
                        CloseOutClaw,
                        new SleepAction(0.25),
                        new ParallelAction(
                                score3,
                                ODumpBox,
                                new SequentialAction(
                                        new SleepAction(2.2),
                                        SpecLiftUp
                                )
                        ),
                        SpecLiftUp,
                        new ParallelAction(
                                OpenOutClaw,
                                LiftDown,
                                OutTuck
                        ),
                        take4,
                        CloseOutClaw,
                        new SleepAction(0.25),
                        ODumpBox,
                        new SleepAction(0.4),
                        new ParallelAction(
                                score4,
                                new SequentialAction(
                                        new SleepAction(2),
                                        new ParallelAction(
                                                SpecLiftUp,
                                                new SleepAction(1.25)
                                        ),
                                        new ParallelAction(
                                                OpenOutClaw, park
                                        )
                                )
                        )

                )
        );
    }
}
