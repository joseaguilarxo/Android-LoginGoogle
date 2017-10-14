package com.joseaguilar.logingoogle_android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
//ACTIVIDAD PRINCIPAL !!
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    //Paso 1: creamos lo siguiente:
    // el GoogleApiClient que nos servira principalmente para autenticarnos en google
    // el SignInButton (ver layout) para el metodo accion de login
    // creamos la constante SIGN_IN_CODE = 777
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Paso 2: instanciamos el API de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() //con este metodo traera el correo del usuario
                .build();

        //Paso 3: inicializamos el googleApiclient -- el googleApiClient sirve como intermediario entre las APIS y la app
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) //este metodo permite gestionar el ciclo de vida del googleApiClient --> en el 2do this te pedira implementar el metodo onConnectionFailed
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso) //con este metodo hacemos el puente entre este proyecto y el API de google
                .build();

        //Paso 4: referenciamos el boton signinButton y le damos un evento onclick
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(signInButton.SIZE_ICON_ONLY); //personalizacion del boton --> solo aparecera el icono (*OPCIONAL*)
        signInButton.setColorScheme(SignInButton.COLOR_DARK);//personalizacion del boton --> cambiara de color (*OPCIONAL*)

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aca colocaremos como accion que abra el selector o inicio de session de una cuenta google --> googleApiClient
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //-----------------------------------------------------------------------------------------
    // Paso 5 : implementamos el metodo onActivityResult el cual se encargara de recepcionar los resultados del logeo
    //onActivityResult --> valida que el login sea exitoso
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //creamos condicional para saber si es el resultado que nos interesa
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data); // con este metodo "result" manejaremos el control de los resultados
            controlSignInResult(result); // creamos el metodo donde trabajaremos el control de resultados
        }
    }

    // controlSignInResult --> administra la accion que se tomara con el login
    private void controlSignInResult(GoogleSignInResult result) {
        //validamos si el login fue exitoso
        if (result.isSuccess()) {
            //creamos un metodo que se encarge realizar la accion de logeo
            goLogin();
        } else {
            Toast.makeText(this, R.string.Error_Login, Toast.LENGTH_SHORT).show(); // el R.String.ERROR_LOGIN  es un mensaje que se crea en el archivo string
        }
    }

    //Realiza la accion que nosotros queramos
    private void goLogin() {
        Intent x = new Intent(this,MainActivity.class);
       x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //se suele agregar flags a los intent para que al momento de pasar de una actividad a otra no se quede colgada con informacion innecesaria
        startActivity(x);
    }
    //-----------------------------------------------------------------------------------------
}
