package classe;

import dao.PessoaDao;
import dao.FuncionarioDao;
import dao.ProjetoDao;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal para testar as operações CRUD e regras de negócio da aplicação.
 * Fornece um menu interativo para gerenciar pessoas, funcionários e projetos.
 */
public class Principal {
    /**
     * Método principal que exibe um menu interativo e executa operações com base na escolha do usuário.
     *
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Instâncias dos DAOs para gerenciar as entidades
        PessoaDao pessoaDao = new PessoaDao();
        FuncionarioDao funcionarioDao = new FuncionarioDao();
        ProjetoDao projetoDao = new ProjetoDao();

        // Laço para exibir o menu até o usuário escolher sair
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Cadastrar Pessoa");
            System.out.println("2 - Listar Pessoas");
            System.out.println("3 - Cadastrar Funcionário");
            System.out.println("4 - Excluir Funcionário");
            System.out.println("5 - Cadastrar Projeto");
            System.out.println("6 - Listar Projetos");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int opcao = sc.nextInt();
            sc.nextLine(); // Limpa o buffer do scanner

            try {
                switch (opcao) {
                    case 1:
                        // Cadastro de uma nova pessoa
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        // Cria um objeto Pessoa com ID 0 (será gerado automaticamente)
                        Pessoa pessoa = new Pessoa(0, nome, email);
                        // Insere a pessoa no banco (Regra de Negócio 5: mensagem de sucesso)
                        pessoaDao.inserir(pessoa);
                        break;

                    case 2:
                        // Lista todas as pessoas cadastradas
                        List<Pessoa> pessoas = pessoaDao.listar();
                        if (pessoas.isEmpty()) {
                            System.out.println("Nenhuma pessoa cadastrada.");
                        } else {
                            System.out.println("Pessoas cadastradas:");
                            for (Pessoa p : pessoas) {
                                System.out.println(p);
                            }
                        }
                        break;

                    case 3:
                        // Cadastro de um funcionário baseado em uma pessoa existente
                        System.out.print("ID da Pessoa já cadastrada: ");
                        int idPessoa = sc.nextInt();
                        sc.nextLine(); // Limpa o buffer
                        // Verifica se a pessoa existe (Regra de Negócio 1)
                        Pessoa pessoaExistente = pessoaDao.buscarPorId(idPessoa);
                        if (pessoaExistente == null) {
                            throw new SQLException("Pessoa com ID " + idPessoa + " não existe.");
                        }
                        System.out.print("Matrícula (ex. F001): ");
                        String matricula = sc.nextLine();
                        System.out.print("Departamento: ");
                        String departamento = sc.nextLine();
                        // Cria um objeto Funcionario com os dados fornecidos
                        Funcionario funcionario = new Funcionario(idPessoa, pessoaExistente.getNome(),
                                pessoaExistente.getEmail(), matricula, departamento);
                        // Insere o funcionário no banco (Regra de Negócio 1 será validada pelo DAO)
                        funcionarioDao.inserir(funcionario);
                        break;

                    case 4:
                        // Exclusão de um funcionário
                        System.out.print("ID do Funcionário a excluir: ");
                        int idFunc = sc.nextInt();
                        // Exclui o funcionário (Regra de Negócio 3: proíbe se vinculado a projeto)
                        funcionarioDao.excluir(idFunc);
                        break;

                    case 5:
                        // Cadastro de um novo projeto
                        System.out.print("Nome do Projeto: ");
                        String nomeProj = sc.nextLine();
                        System.out.print("Descrição: ");
                        String desc = sc.nextLine();
                        System.out.print("ID do Funcionário Responsável: ");
                        int idResp = sc.nextInt();
                        // Cria um objeto Projeto
                        Projeto projeto = new Projeto(0, nomeProj, desc, idResp);
                        // Insere o projeto no banco (Regra de Negócio 2: verifica funcionário)
                        projetoDao.inserir(projeto);
                        break;

                    case 6:
                        // Lista todos os projetos cadastrados
                        List<Projeto> projetos = projetoDao.listar();
                        if (projetos.isEmpty()) {
                            System.out.println("Nenhum projeto cadastrado.");
                        } else {
                            System.out.println("Projetos cadastrados:");
                            for (Projeto proj : projetos) {
                                System.out.println(proj);
                            }
                        }
                        break;

                    case 0:
                        // Encerra o programa
                        System.out.println("Encerrando...");
                        sc.close();
                        return;

                    default:
                        // Opção inválida
                        System.out.println("Opção inválida! Escolha uma opção válida.");
                }
            } catch (SQLException e) {
                // Exibe erro no console (Regra de Negócio 4)
                System.err.println("Erro: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // Exibe erros de validação dos objetos (Regra de Negócio 4)
                System.err.println("Erro de validação: " + e.getMessage());
            }
        }
    }
}