package classe;

/**
 * Representa um projeto da empresa, vinculado a um funcionário responsável.
 */
public class Projeto {
    // Identificador único do projeto (gerado automaticamente)
    private int id;
    // Título do projeto
    private String nome;
    // Descrição detalhada do projeto
    private String descricao;
    // ID do funcionário responsável
    private int idFuncionario;

    /**
     * Construtor padrão para a classe Projeto.
     */
    public Projeto() {
        // Construtor vazio para flexibilidade
    }

    /**
     * Construtor com parâmetros para inicializar um Projeto.
     *
     * @param id            Identificador único do projeto (gerado automaticamente)
     * @param nome         Título do projeto
     * @param descricao    Descrição detalhada do projeto
     * @param idFuncionario ID do funcionário responsável
     */
    public Projeto(int id, String nome, String descricao, int idFuncionario) {
        this.id = id;
        // Usa setters para validação
        setNome(nome);
        setDescricao(descricao);
        this.idFuncionario = idFuncionario;
    }

    /**
     * Obtém o ID do projeto.
     *
     * @return o ID do projeto
     */
    public int getId() {
        return id;
    }

    /**
     * Define o ID do projeto.
     *
     * @param id o ID do projeto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome do projeto.
     *
     * @return o nome do projeto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do projeto.
     *
     * @param nome o nome do projeto
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     */
    public void setNome(String nome) {
        // Valida se o nome é válido (Regra de Negócio 4)
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do projeto não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    /**
     * Obtém a descrição do projeto.
     *
     * @return a descrição do projeto
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do projeto.
     *
     * @param descricao a descrição do projeto
     * @throws IllegalArgumentException se a descrição for nula ou vazia
     */
    public void setDescricao(String descricao) {
        // Valida se a descrição é válida (Regra de Negócio 4)
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição do projeto não pode ser nula ou vazia.");
        }
        this.descricao = descricao;
    }

    /**
     * Obtém o ID do funcionário responsável.
     *
     * @return o ID do funcionário
     */
    public int getIdFuncionario() {
        return idFuncionario;
    }

    /**
     * Define o ID do funcionário responsável.
     *
     * @param idFuncionario o ID do funcionário
     * @throws IllegalArgumentException se o ID for inválido
     */
    public void setIdFuncionario(int idFuncionario) {
        // Valida se o ID do funcionário é positivo (Regra de Negócio 4)
        if (idFuncionario <= 0) {
            throw new IllegalArgumentException("ID do funcionário deve ser um valor positivo.");
        }
        this.idFuncionario = idFuncionario;
    }

    /**
     * Retorna uma representação em string do projeto.
     *
     * @return string com os detalhes do projeto
     */
    @Override
    public String toString() {
        return "Projeto [id=" + id + ", nome=" + nome + ", descricao=" + descricao +
               ", idFuncionario=" + idFuncionario + "]";
    }
}