package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private SendableChooser<String>  autoChooser = new SendableChooser<>();

  //MUÑECA
  private PWMVictorSPX Muneca = new PWMVictorSPX(7);
  private PWMVictorSPX Muneca2 = new PWMVictorSPX(8);

   MotorControllerGroup munecas = new MotorControllerGroup(Muneca, Muneca2);

  //RECOLETOR
  private PWMVictorSPX shoot = new PWMVictorSPX(9);

   //HOMBRO
   private PWMVictorSPX Hombro = new PWMVictorSPX(6);
   private PWMVictorSPX Hombro2 = new PWMVictorSPX(5);

   //GRUPO MOTORES HOMBRO
   public MotorControllerGroup  hombros = new MotorControllerGroup(Hombro,Hombro2);

   // CHASIS
   public PWMSparkMax MotorderechoD = new PWMSparkMax(1);
   public PWMSparkMax MotorderechoU = new PWMSparkMax(3);
   public PWMSparkMax MotorIzquierdoU = new PWMSparkMax(4);
   public PWMSparkMax MotorIzquierdoD = new PWMSparkMax(2);
   
   //GRUPO CONTROLES TRACCION
   public MotorControllerGroup MotoresDERECHOS = new MotorControllerGroup(MotorderechoD,MotorderechoU);
   public MotorControllerGroup MotoresIZQUIERDO = new MotorControllerGroup(MotorIzquierdoD,MotorIzquierdoU);
 
   //TRACCION
   public DifferentialDrive Traccion = new DifferentialDrive(MotoresDERECHOS,MotoresIZQUIERDO);

  //MOTORES CODO
  private CANSparkMax codo = new CANSparkMax(2, MotorType.kBrushed);
  private CANSparkMax codo2 = new CANSparkMax(1, MotorType.kBrushed);
  MotorControllerGroup codos = new MotorControllerGroup(codo, codo2);

  //JOYSTICK
  public Joystick control = new Joystick(0);
  public XboxController xbox = new XboxController(1);

  // TIMER
  private Timer timer = new Timer();

  @Override
  public void robotInit() {
    //SELECTOR AUTONOMO
    autoChooser.addOption("Modo 1", "modo1");
    autoChooser.addOption("Modo 2", "modo2");
    autoChooser.addOption("Modo 3", "modo3");
    autoChooser.addOption("Modo 4", "modo4");
    SmartDashboard.putData("Modo Autonomo", autoChooser);
    
    CameraServer.startAutomaticCapture();
  }

  @Override
  public void robotPeriodic() {
    //VALORES SMARTDASHBOARD
    SmartDashboard.putNumber("hombro", codo.get());
  
    //TRACCION VALOR
    LiveWindow.enableTelemetry(Traccion);
    SmartDashboard.putNumber("vel", control.getRawAxis(3));
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();    
  }

  @Override
  public void autonomousPeriodic() {
    //BALANCE AUTONOMO
    String autoSelected = autoChooser.getSelected();
    switch(autoSelected) {
      //AUTONOMO CENTRO
      case "modo1":
      //balance
      if(timer.get()<1){
        if(timer.get() < 1){
         }else if (timer.get()>1 && timer.get() < 3)  {
          hombros.set(0.5);
         }else if (timer.get()>3 && timer.get() < 5){
          hombros.set(0);
          shoot.set(0.4);
         }else if (timer.get()>5 && timer.get() < 6){
          shoot.set(0);
        }else if (timer.get()>6 && timer.get() < 8){
          hombros.set(-0.5);
        }else if (timer.get()>8 && timer.get() < 9){
          hombros.set(0);
        }
      }
      break;
      case "modo2":
         if(timer.get() < 0.8){
          Muneca.set(0.5);
         }else if (timer.get()>1 && timer.get() < 1.5)  {
          Muneca.set(0);
          shoot.set(-1);
         }else if (timer.get()>1.5 && timer.get() < 3){
          shoot.set(0);
          Muneca.set(-0.7);
         }else if (timer.get()>3 && timer.get() < 5.1){
          MotorderechoD.set(-0.85);
          MotorIzquierdoD.set(0.85);
          MotorderechoU.set(-0.85);
          MotorIzquierdoU.set(0.85);
          Muneca.set(0);
        }else if (timer.get()>5.1 && timer.get() < 5.4){
          MotorderechoD.set(-0.7);
          MotorIzquierdoD.set(-0.7);
          MotorderechoU.set(-0.7);
          MotorIzquierdoU.set(-0.7);
        }
      break;
      case "modo3":
      if(timer.get() < 0.8){
        Muneca.set(0.5);
       }else if (timer.get()>1 && timer.get() < 3)  {
        Muneca.set(0);
        shoot.set(-1);
       }else if (timer.get()>3 && timer.get() < 4){
        shoot.set(0);
        Muneca.set(-0.7);
       }
    break;
    }
  }

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    //MOVIMIENTO
    Traccion.arcadeDrive(control.getZ() * (control.getRawAxis(3) - 1), -control.getY()* (control.getRawAxis(3) - 1));

    //BRAZO
    hombros.set(xbox.getRawAxis(5)  * (control.getRawAxis(3) - 1) );

    //codo
    codos.set(xbox.getRawAxis(1)* (control.getRawAxis(3) - 1) );

    // muñecas con joystick
    Muneca.set(xbox.getRawAxis(1) * 0.7);
     
    //MOV. MUÑECA
    if(xbox.getXButtonPressed()){
      Muneca.set(1);
    } else if(xbox.getXButtonReleased()){
      Muneca.set(0);
    }
    
    //MOV. MUÑECA REVERSA
    if(xbox.getYButtonPressed()){
      Muneca.set(-1);
    } else if(xbox.getYButtonReleased()){
      Muneca.set(0);
    }
     
    //SHOOT
    if(xbox.getAButtonPressed()){
      shoot.set(0.7);
    } else if(xbox.getAButtonReleased()){
     shoot.set(0);
    }

    //SHOOT INVERSED
    if(xbox.getBButtonPressed()){
      shoot.set(-0.7);
    } else if(xbox.getBButtonReleased()){
     shoot.set(0);
    }
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}

}