Aplicação de Chat com Sockets em Java (RELATÓRIO)

Este projeto consiste no desenvolvimento de uma aplicação de comunicação em tempo real baseada no modelo cliente-servidor, utilizando a linguagem Java e o protocolo de comunicação via Sockets TCP.

A solução foi concebida para permitir a troca de mensagens entre múltiplos usuários de forma simultânea, integrando uma interface gráfica intuitiva para melhorar a experiência de utilização.

Autores
Levi Manuel Alberto
Gonçalves Pinto Domingos
Matondo Alberto Mauricio
André Miguel Kasowa Mateus

Objetivo
Desenvolver uma aplicação de chat funcional que permita a comunicação entre vários utilizadores em tempo real, utilizando uma arquitetura cliente-servidor com suporte a múltiplas conexões simultâneas.

Tecnologias Utilizadas
Linguagem de Programação: Java
Ambiente de Desenvolvimento (IDE): IntelliJ IDEA
Comunicação em Rede: Sockets TCP
Interface Gráfica: JavaFX

Arquitetura do Sistema
O sistema segue o modelo clássico de redes:

Servidor
Inicializa em uma porta específica
Aguarda conexões de clientes
Gerencia múltiplas conexões através de threads (multithreading)
Responsável por distribuir mensagens para todos os clientes conectados (broadcast)

Cliente
Conecta-se ao servidor
Interface gráfica para interação com o utilizador
Permite envio e receção de mensagens em tempo real

Funcionalidades
Comunicação em tempo real entre múltiplos usuários
Suporte a múltiplos clientes simultâneos
Identificação de usuários nas mensagens
Envio e receção de mensagens (broadcast)
Interface gráfica amigável
Atualização dinâmica das mensagens

Interface Gráfica
A interface foi desenvolvida com JavaFX, proporcionando uma melhor experiência ao utilizador.
Componentes principais:
Campo de texto para digitação de mensagens
Área de exibição das mensagens
Botão de envio
Identificação visual dos participantes
Essa abordagem substitui o uso do console por uma interação mais moderna e intuitiva.

Resultados Obtidos
O sistema desenvolvido apresenta os seguintes resultados:
Comunicação eficiente em tempo real
Interface intuitiva e de fácil utilização
Execução simultânea de vários clientes
Estabilidade na troca de mensagens via rede

Conceitos Aplicados
Este projeto envolve a aplicação prática de conceitos fundamentais de:
Redes de Computadores
Programação Concorrente (Threads)
Arquitetura Cliente-Servidor
Interfaces Gráficas em Java
Comunicação via Sockets

Conclusão
O desenvolvimento desta aplicação permitiu consolidar conhecimentos na área de redes e programação orientada a objetos, demonstrando a integração eficaz entre comunicação distribuída e interfaces gráficas.
A solução final apresenta-se funcional, escalável e com boa usabilidade, constituindo uma base sólida para futuras melhorias, como mensagens privadas, envio de ficheiros e autenticação de utilizadores.

Como Executar o Projeto
Clonar o repositório:
git clone https://github.com/andrekasowa9GrupoBigO-dot/chat-java-sockets
Abrir o projeto no IntelliJ IDEA
Executar primeiro o Servidor
Executar um ou mais Clientes
