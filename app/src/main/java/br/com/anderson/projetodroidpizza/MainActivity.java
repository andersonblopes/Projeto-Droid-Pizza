package br.com.anderson.projetodroidpizza;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private Button btnIniciarAtendimento;
    private static final int PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_PIZZA = 1;
    private static final int PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_BEBIDA = 2;
    private static final int PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_CONFIRM_PEDIDO = 3;
    private Double valorPedido = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIniciarAtendimento = (Button) findViewById(R.id.btn_iniciar);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(new Locale("pt"));
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        iniciarcapturaFala(Integer.parseInt(utteranceId));
                    }

                    @Override
                    public void onError(String utteranceId) {
                    }
                });
            }
        });

        btnIniciarAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak("Bem vindo ao Droid Pizza. Escolha o sabor da sua pizza.",TextToSpeech.QUEUE_FLUSH, null, String.valueOf(PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_PIZZA));
            }
        });

    }

    private void iniciarcapturaFala(Integer flagReconhecimento){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora");
        startActivityForResult(intent, flagReconhecimento);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> listaPossiveisfalas = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        //Verifica se a lista está preenchida
        if(listaPossiveisfalas != null || listaPossiveisfalas.size() > 0){
            boolean achou = false;
            //Percorrer a lista
            for(int i = 0; i < listaPossiveisfalas.size(); i++){
                String resposta = listaPossiveisfalas.get(i);
                if(requestCode == PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_PIZZA){
                    if(resposta.equalsIgnoreCase("Presunto") || resposta.equalsIgnoreCase("Pizza Presunto") || resposta.equalsIgnoreCase("Pizza de presunto")){
                        achou = true;
                        valorPedido += 28.00;
                        textToSpeech.speak("Entendi. Pizza de presunto. Escolha sua bebida.", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_BEBIDA));
                    }
                    if(resposta.equalsIgnoreCase("Portuguesa") || resposta.equalsIgnoreCase("Pizza Portuguesa") || resposta.equalsIgnoreCase("Pizza de presunto")){
                        achou = true;
                        valorPedido += 30.00;
                        textToSpeech.speak("Entendi. Pizza Portuguesa. Escolha sua bebida.", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_BEBIDA));
                    }
                    if(resposta.equalsIgnoreCase("Camarão") || resposta.equalsIgnoreCase("Pizza Camarão") || resposta.equalsIgnoreCase("Pizza de presunto")){
                        achou = true;
                        valorPedido += 40.00;
                        textToSpeech.speak("Entendi. Pizza de Camarão. Escolha sua bebida.", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_BEBIDA));
                    }
                }else if (requestCode == PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_BEBIDA){
                    if(resposta.equalsIgnoreCase("Coca") || resposta.equalsIgnoreCase("coca cola") || resposta.equalsIgnoreCase("coquinha")){
                        achou = true;
                        valorPedido += 7.00;
                        textToSpeech.speak("Entendi. Coca cola. O valor do seu pedido é:"+String.valueOf(valorPedido)+" . Confirma o seu pedido?", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_CONFIRM_PEDIDO));
                    }
                    if(resposta.equalsIgnoreCase("Guaraná") || resposta.equalsIgnoreCase("guará") || resposta.equalsIgnoreCase("guarana")){
                        achou = true;
                        valorPedido += 5.00;
                        textToSpeech.speak("Entendi. Guaraná. O valor do seu pedido é:"+String.valueOf(valorPedido)+" . Confirma o seu pedido?", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_CONFIRM_PEDIDO));
                    }
                }else if(requestCode == PARAM_REQUISICAO_PARA_PEGAR_TEXTO_FALADO_CONFIRM_PEDIDO){
                    if(resposta.equalsIgnoreCase("Sim") || resposta.equalsIgnoreCase("Ok") || resposta.equalsIgnoreCase("Yes") || resposta.equalsIgnoreCase("Confirmado")){
                        achou = true;
                        textToSpeech.speak("Ok. Pedido confirmado. Em 50 minutos seu pedido será entregue em sua residência."+"Droid Pizza! Grato pela preferência.", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(null));
                    }
                    if(resposta.equalsIgnoreCase("Não") || resposta.equalsIgnoreCase("not") || resposta.equalsIgnoreCase("Sair")){
                        achou = true;
                        textToSpeech.speak("Ok. Pedido cancelado. Faça seu pedido novamente.", TextToSpeech.QUEUE_FLUSH, null, String.valueOf(null));
                    }
                    valorPedido = 0.0;
                }
                if(!achou){
                    Toast.makeText(this, "Não entendi.",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}