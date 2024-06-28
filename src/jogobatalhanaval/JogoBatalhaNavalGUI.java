
package jogobatalhanaval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class JogoBatalhaNavalGUI extends JFrame {

    private static final int TAMANHO_CELULA = 50; // Tamanho das células do tabuleiro
    private static final int TAMANHO_TABULEIRO = 10; // Tamanho do tabuleiro (10x10)

    private JButton[][] botoesJogador;
    private JButton[][] botoesOponente;
    private EstadoPosicao[][] tabuleiroJogador;
    private EstadoPosicao[][] tabuleiroOponente;

    private int naviosRestantesJogador = 5;
    private int naviosRestantesOponente = 5;

    private enum EstadoPosicao {
        VAZIO, OCUPADO, ATINGIDO, AGUA
    }

    public JogoBatalhaNavalGUI(String nomeJogador) {
        super("Batalha Naval");

        // Inicializa os tabuleiros
        tabuleiroJogador = new EstadoPosicao[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        tabuleiroOponente = new EstadoPosicao[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        // Inicializa o estado dos tabuleiros
        inicializarEstadosTabuleiros();

        // Configura a janela principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400); // Tamanho da janela
        setLayout(new GridLayout(1, 2)); // Layout da janela (1 linha, 2 colunas)

        // Inicializa os tabuleiros visuais
        inicializarTabuleiros();

        // Adiciona os tabuleiros à janela
        add(montarPainelTabuleiro(botoesJogador));
        add(montarPainelTabuleiro(botoesOponente));

        // Posiciona os navios do jogador e do oponente aleatoriamente
        posicionarNavios(tabuleiroJogador);
        posicionarNavios(tabuleiroOponente);

        // Exibe a janela
        setVisible(true);
    }

    private void inicializarEstadosTabuleiros() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiroJogador[i][j] = EstadoPosicao.VAZIO;
                tabuleiroOponente[i][j] = EstadoPosicao.VAZIO;
            }
        }
    }

    private void inicializarTabuleiros() {
        // Inicializa os botões do jogador
        botoesJogador = new JButton[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                JButton botao = new JButton();
                final int linha = i, coluna = j;
                botoesJogador[i][j] = botao;
                botoesJogador[i][j].setEnabled(false); // Desabilita os botões do jogador inicialmente
            }
        }

        // Inicializa os botões do oponente (com ação para o jogador)
        botoesOponente = new JButton[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                JButton botao = new JButton();
                final int linha = i, coluna = j;
                botao.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        realizarAtaqueJogador(linha, coluna);
                    }
                });
                botoesOponente[i][j] = botao;
            }
        }
    }

    private void posicionarNavios(EstadoPosicao[][] tabuleiro) {
        Random random = new Random();
        int naviosColocados = 0;
        while (naviosColocados < 5) { // Coloca 5 navios
            int linha = random.nextInt(TAMANHO_TABULEIRO);
            int coluna = random.nextInt(TAMANHO_TABULEIRO);
            if (tabuleiro[linha][coluna] != EstadoPosicao.OCUPADO) {
                tabuleiro[linha][coluna] = EstadoPosicao.OCUPADO;
                naviosColocados++;
            }
        }
    }

    private JPanel montarPainelTabuleiro(JButton[][] botoes) {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(TAMANHO_TABULEIRO, TAMANHO_TABULEIRO));

        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                JButton botao = botoes[i][j];
                botao.setPreferredSize(new Dimension(TAMANHO_CELULA, TAMANHO_CELULA));
                painel.add(botao);
            }
        }

        return painel;
    }

    private void realizarAtaqueJogador(int linha, int coluna) {
        if (tabuleiroOponente[linha][coluna] == EstadoPosicao.OCUPADO) {
            tabuleiroOponente[linha][coluna] = EstadoPosicao.ATINGIDO;
            botoesOponente[linha][coluna].setBackground(Color.RED); // Acerto
            naviosRestantesOponente--;
            if (naviosRestantesOponente == 0) {
                JOptionPane.showMessageDialog(this, "Parabéns! Você venceu o jogo.");
                reiniciarJogo();
            }
        } else {
            tabuleiroOponente[linha][coluna] = EstadoPosicao.AGUA;
            botoesOponente[linha][coluna].setBackground(Color.BLUE); // Água
            // Simula o turno do oponente após o jogador atacar
            jogarTurnoOponente();
        }
        botoesOponente[linha][coluna].setEnabled(false); // Desabilita o botão após o ataque
    }

    private void jogarTurnoOponente() {
        Random random = new Random();
        int linha, coluna;
        do {
            linha = random.nextInt(TAMANHO_TABULEIRO);
            coluna = random.nextInt(TAMANHO_TABULEIRO);
        } while (tabuleiroJogador[linha][coluna] == EstadoPosicao.ATINGIDO || tabuleiroJogador[linha][coluna] == EstadoPosicao.AGUA);

        if (tabuleiroJogador[linha][coluna] == EstadoPosicao.OCUPADO) {
            tabuleiroJogador[linha][coluna] = EstadoPosicao.ATINGIDO;
            botoesJogador[linha][coluna].setBackground(Color.RED); // Acerto
            naviosRestantesJogador--;
            if (naviosRestantesJogador == 0) {
                JOptionPane.showMessageDialog(this, "Você perdeu o jogo. Tente novamente.");
                reiniciarJogo();
            }
        } else {
            tabuleiroJogador[linha][coluna] = EstadoPosicao.AGUA;
            botoesJogador[linha][coluna].setBackground(Color.BLUE); // Água
        }
        botoesJogador[linha][coluna].setEnabled(false); // Desabilita o botão após o ataque
    }

    private void reiniciarJogo() {
        // Reinicia o jogo
        inicializarEstadosTabuleiros();
        naviosRestantesJogador = 5;
        naviosRestantesOponente = 5;

        // Posiciona os navios novamente
        posicionarNavios(tabuleiroJogador);
        posicionarNavios(tabuleiroOponente);

        // Atualiza visualmente os tabuleiros
        atualizarTabuleiroVisual(botoesJogador, tabuleiroJogador);
        atualizarTabuleiroVisual(botoesOponente, tabuleiroOponente);

        // Habilita os botões do jogador e desabilita os do oponente
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                botoesJogador[i][j].setEnabled(true);
                botoesOponente[i][j].setEnabled(false);
            }
        }
    }

    private void atualizarTabuleiroVisual(JButton[][] botoes, EstadoPosicao[][] tabuleiro) {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                switch (tabuleiro[i][j]) {
                    case VAZIO:
                        botoes[i][j].setBackground(null); // Sem cor de fundo
                        break;
                    case OCUPADO:
                        botoes[i][j].setBackground(Color.GRAY); // Navio do jogador
                        break;
                    case ATINGIDO:
                        botoes[i][j].setBackground(Color.RED); // Acerto
                        break;
                    case AGUA:
                        botoes[i][j].setBackground(Color.BLUE); // Água
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JogoBatalhaNavalGUI("Jogador"));
    }
}
