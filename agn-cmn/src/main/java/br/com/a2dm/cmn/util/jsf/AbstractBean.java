package br.com.a2dm.cmn.util.jsf;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.component.UIDataTable;

import br.com.a2dm.cmn.util.A2DMHbNgc;
import br.com.a2dm.cmn.util.others.Utilitarios;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Classe base para ser usada por cadastros. Cont�m o fluxo base de troca
 * pain�is de edi��o.
 * 
 * @author Carlos Diego
 * @param <Entity> - vo a ser utilizado como filtro e como base para alterar e
 *           inserir os registros no banco de dados
 * @param <Business> - classe de neg�cio respons�vel por extrair e persistir as
 *           informa��es no banco de dados
 */
public abstract class AbstractBean<Entity, Business extends A2DMHbNgc<Entity>>
{

   public static final String USUARIO_LOGADO = "loginUsuario";

   /**
    * Constante para ser usada como chave (nome) a ser armazenado no
    * pageFlowScope o resultado das consultas.
    */
   public static final String SEARCH_RESULT = "searchResult";

   /**
    * Constante para ser usada como chave (nome) a ser armazenado no
    * pageFlowScope os filtros das consultas.
    */
   public static final String SEARCH_PARAMETERS = "searchParameters";

   /**
    * Constante para ser usada como chave (nome) a ser armazenado no
    * pageFlowScope o vo que est� sendo alterado ou inclu�do no banco de dados
    */
   public static final String ENTITY = "entity";

   /**
    * Constante que indica o estado de altera��o da p�gina
    */
   public static final String STATE_EDIT = "edit";

   /**
    * Constante que indica o estado de inclus�o da p�gina
    */
   public static final String STATE_INSERT = "insert";

   /**
    * Constante que indica o estado de exclus�o da p�gina
    */
   public static final String STATE_DELETE = "delete";

   /**
    * Constante que indica o estado de visualiza��o da p�gina
    */
   public static final String STATE_VIEW = "view";

   /**
    * Constante que indica o estado de pesquisa da p�gina
    */
   public static final String STATE_SEARCH = "search";

   /**
    * HashMap que armazena os complementos para o t�tulo da tela
    * 
    * @see #getFullTitle()
    */
   protected static final Map<String, String> titles = new HashMap<String, String>();

   /**
    * Vari�vel que indica qual o id da p�gina no arquivo faces-config.xml
    */
   protected String ACTION_SEARCH = "search";

   /**
    * Indica o nome do arquivo .jasper que foi constriudo para exibir os dados
    * da consulta na forma de relat�rio pdf
    */
   protected String REPORT_NAME = null;

   /**
    * Pacote onde se encontra o arquivo .jasper definido pela propriedade
    * {@link #REPORT_NAME}
    */
   protected String PACKAGE_REPORT_NAME = "relatorio";

   /**
    * Complemento a ser adicionado ao t�tulo da p�gina para indica que a mesma
    * se encontra no estado de pesquisa
    */
   protected static String titleSearch = "Consultar";

   /**
    * Complemento a ser adicionado ao t�tulo da p�gina para indica que a mesma
    * se encontra no estado de altera��o
    */
   protected static String titleEdit = "Alterar";

   /**
    * Complemento a ser adicionado ao t�tulo da p�gina para indica que a mesma
    * se encontra no estado de inclus�o
    */
   protected static String titleInsert = "Cadastrar";

   /**
    * Complemento a ser adicionado ao t�tulo da p�gina para indica que a mesma
    * se encontra no estado de visualiza��o
    */
   protected static String titleView = "Visualizar";

   static
   {
      titles.put(null, titleSearch); // primeiro acesso a p�gina
      titles.put(STATE_SEARCH, titleSearch);
      titles.put(STATE_INSERT, titleInsert);
      titles.put(STATE_EDIT, titleEdit);
      titles.put(STATE_VIEW, titleView);
   }

   /**
    * Painel de Edi��o
    */
   private UIComponent pnlEditing;

   /**
    * Id do componente que exibe os dados. Nesta implementa��o � esperada uma
    * tabela, mas as subclasses podem alterar.
    */
   private UIDataTable tblData;

   /**
    * Mensagem para quando o usu�rio n�o escolher nenhum item.
    */
   protected String noItemSelectedMessage = "Nenhum item selecionado.";

   /**
    * Mensagem para quando a busca n�o retornar nenhum resultado.
    */
   protected String noResultMessage = "Nenhum registro encontrado.";

   /**
    * Chave no arquivo de recurso para o t�tulo da p�gina
    */
   protected String pageTitle = "Nenhum t�tulo definido.";

   /**
    * Entidade usada para as opera��es de editar, excluir, inserir. Os
    * componentes de edi��o far�o refer�ncia a esse objeto.
    */
   private Entity entity;

   /**
    * Objeto que armazena os par�metros da busca. Usada em uma busca do tipo
    * "findByExample".
    */
   private Entity searchObject;

   /**
    * Lista de resultados obtidos na consulta
    */
   private List<Entity> searchResult;

   /**
    * Estado atual da p�gina
    */
   private String currentState;

   /**
    * Interface com a camada de neg�cios.
    */
   private Business business;

   /**
    * Construtor que recebe a classe de neg�cio como par�metro
    * 
    * @param business - classe de neg�cio a ser utilizada no bean
    */
   public AbstractBean(Business business)
   {
      this();
      this.business = business;
   }

   /**
    * Construtor padr�o
    */
   public AbstractBean()
   {
      super();
      init();
   }

   /**
    * Incializa��o da classe
    */
   public void postConstruct()
   {
      try
      {
         // inicia a entidade com uma inst�ncia vazia.
         if (getEntity() == null)
         {
            setEntity(getNewEntityInstance());
         }
         if (getSearchObject() == null)
         {
            setSearchObject(getNewEntityInstance());
         }
      }
      catch (Exception e)
      {
         System.out
               .println("Os objetos n�o puderam ser instanciados corretamente.");
         // n�o relan�a a exce��o porque esses objetos podem ser informados
         // atrav�s de propriedades.
      }
   }

   /**
    * Utilizando o par�metro passado por generics
    * 
    * @return - a entidade utilizada como filtro, e nas opera��o de alterar e
    *         incluir
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   protected Entity getNewEntityInstance()
   {
      try
      {
         return (Entity) ((Class) ((java.lang.reflect.ParameterizedType) this
               .getClass().getGenericSuperclass()).getActualTypeArguments()[0])
               .newInstance();
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         return null;
      }
   }

   /**
    * Processa a inicializa��o dos dados
    */
   public void init()
   {
      setCurrentState(null);
      setSearchResult(null);
      setEntity(getNewEntityInstance());
      setSearchObject(getNewEntityInstance());
   }

   /**
    * Evento que dispara a inicializa��o. Pode ser usado por um ActionSource
    * (bot�o, link, menu ou similar) para inicar a implementa��o de um caso de
    * uso que fa�a parte da hierarquia dessa classe.
    * 
    * @param event - Objeto que representa o evento.
    */
   public void init(ActionEvent event)
   {
      init();
   }

   /**
    * M�todo que preenche as lista que deve ser utilzadas como filtro na
    * pesquisa
    * 
    * @return - id da p�gina que deve ser redirecionada a aplica��o
    */
   public String preparaPesquisar()
   {
      try
      {
    	  if(validarAcesso(Variaveis.ACAO_PREPARA_PESQUISAR))
    	  {
    		  setValoresDefault();
    		  setCurrentState(STATE_SEARCH);
    		  setSearchObject(getNewEntityInstance());
    		  setListaPesquisa();
    		  this.setSearchResult(null);
    	  }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
      return ACTION_SEARCH;
   }

   /**
    * Metodo que deve ser sobreescrito para preencher os valores default que serao
    * utilizadas como filtro na consulta
    * 
    * @throws Exception - erro inesperado da aplicacao
    */
   protected void setValoresDefault() throws Exception
   {
   }
   
   /**
    * M�todo que deve ser sobreescrito para preencher as lista que ser�o
    * utilizadas como filtro na consulta
    * 
    * @throws Exception - erro inesperado da aplica��o
    */
   protected void setListaPesquisa() throws Exception
   {
   }

   /**
    * Implementa a a��o de consulta. Disparado por um evento (actionListener) do
    * UIXCommandButton (
    * <tr:commandButton/>
    * ).
    * 
    * @param event Objeto que representa o evento.
    */
   public void pesquisar(ActionEvent event)
   {
      //if (tblData != null)
      //{
         /*
          * CollectionModel result = new
          * PagingCollectionModel(tblData.getRows(), business,
          * getSearchObject()); setSearchResult(result);
          * tblData.setRendered(result.getRowCount() > 0); if
          * (this.isSearching())
          * result.setSortCriteria(this.getDefaultSortCriteria()); //
          * tblData.setSortCriteria(this.getDefaultSortCriteria()); if
          * (result.getRowCount() <= 0) {
          * FacesContext.getCurrentInstance().addMessage( null, new
          * FacesMessage(FacesMessage.SEVERITY_WARN,noResultMessage, null)); }
          * setCurrentState(STATE_SEARCH);
          */

         try
         {
        	 if(validarAcesso(Variaveis.ACAO_PESQUISAR))
        	 {
        		 validarPesquisar();
        		 completarPesquisar();
        		 validarCampoTexto();
        		 List<Entity> lista = business.pesquisar(this.getSearchObject(),
        				 getJoinPesquisar());
        		 this.setSearchResult(lista);
        		 completarPosPesquisar();
        		 setCurrentState(STATE_SEARCH);
        	 }
         }
         catch (Exception e)
         {
            FacesMessage message = new FacesMessage(e.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            if(e.getMessage() == null)
           	 FacesContext.getCurrentInstance().addMessage("", message);
            else
           	 FacesContext.getCurrentInstance().addMessage(null, message);
            this.setSearchResult(new ArrayList<Entity>());
         }
      //}
   }

   protected void completarPesquisar() throws Exception
   {
   }

   protected void completarPosPesquisar() throws Exception
   {
   }
   
   /**
    * M�todo que verifica se todos os campos texto preenchidos na pesquisa
    *  tem tamanho superior a 3
    * 
    * @throws Exception - erro inesperado da aplica��o
    */
   protected void validarCampoTexto() throws Exception
   {
	   Method[] metodos = getSearchObject().getClass().getDeclaredMethods();
	   boolean possuiErros = false;
	   if(metodos != null
			   && metodos.length > 0)
	   {
		   for(Method metodo : metodos)
		   {
			   if("java.lang.String".equalsIgnoreCase(metodo.getReturnType().getName())
					   && metodo.getName().length() >= 5
					   && !metodo.getName().substring(0, 5).equalsIgnoreCase("getFl"))
			   {
				   
				   String nomePropriedade = metodo.getName().substring(3, metodo.getName().length());
				   
				   nomePropriedade = nomePropriedade.substring(0, 1).toLowerCase() + nomePropriedade.substring(1, nomePropriedade.length());
				   
				   Field field = getSearchObject().getClass().getDeclaredField(nomePropriedade);
				   field.setAccessible(true);
			   }
		   }
	   }
	   
	   if(possuiErros)
	   {
			FacesMessage message = new FacesMessage("Todos os campos texto preenchidos devem conter no m�nimo 3 caracteres!");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			throw new Exception();
	   }
   }   

   /**
    * M�todo que verifica se todos os campos obrigat�rio da pesquisa foram
    * preenchidos
    * 
    * @throws Exception - erro inesperado da aplica��o
    */
   protected void validarPesquisar() throws Exception
   {
   }

   /**
    * M�todo que verifica se todos os campos obrigat�rio da insercao foram
    * preenchidos
    * 
    * @throws Exception - erro inesperado da aplica��o
    */
   protected void validarInserir() throws Exception
   {
   }
   
   /**
    * Defini��o das jun��es que devem ser realizadas no banco de dados para
    * obten��o de todos os dados que ser�o exibidos como resultados na consulta
    * 
    * @return - mapa de bits, onde cada bit representa a jun��o que deve ser
    *         realizada
    */
   protected int getJoinPesquisar()
   {
      return 0;
   }

   /**
    * Salva os dados da altera��o / inclus�o.
    * 
    * @param event - evento que foi disparado na tela
    */
   public void inserir(ActionEvent event) 
   {
      try
      {
    	  if(validarAcesso(Variaveis.ACAO_INSERIR))
    	  {
    		  validarInserir();
    		  completarInserir();
    		  setEntity(business.inserir(getEntity()));
    		  FacesContext.getCurrentInstance().addMessage(
    				  null,
    				  new FacesMessage(FacesMessage.SEVERITY_INFO,
    						  "Registro inserido com sucesso.", null));
    		  
    		  this.clean(event);
    	  }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   protected void completarInserir() throws Exception
   {
   }

   /**
    * Salva os dados da altera��o / inclus�o.
    * 
    * @param event - evento que foi disparado na tela
    */
   public void alterar(ActionEvent event)
   {
      // update
      try
      {
    	  if(validarAcesso(Variaveis.ACAO_ALTERAR))
    	  {
    		  completarAlterar();
    		  setEntity(business.alterar(getEntity()));
    		  FacesContext.getCurrentInstance().addMessage(
    				  null,
    				  new FacesMessage(FacesMessage.SEVERITY_INFO,
    						  "Registro alterado com sucesso.", null));
    		  
    		  // search(event);
    		  // realiza a pesquisa para preenchimento dos combos
    		  // setSearchLists();
    		  this.clean(event);
    	  }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   protected void completarAlterar() throws Exception
   {
   }

   /**
    * Realiza a a��o que prepara a p�gina para a edi��o dos dados
    * 
    * @param event - evento que foi disparado na tela
    */
   public void preparaAlterar()
   {
      try
      {
    	  if(validarAcesso(Variaveis.ACAO_PREPARA_ALTERAR))
    	  {
    		 // if (this.getSelectedRowEntity() != null)
    		 // {
    		//	  setEntity(getSelectedRowEntity());
    			  
    			  /*
    			   * salva os dados da pesquisa, digitados pelo usu�rio para
    			   * posteriormente restaurar quando voltar para a consulta
    			   */
    			  // if (!STATE_VIEW.equals(getCurrentState())) {
    			  // Se n�o tiver vindo da visualiza��o, guarda. Caso contr�rio, n�o
    			  // armazena
    			  // pois a tela de visualiza��o envia par�metros vazios e sobrescreveria
    			  // o da tela
    			  // de consulta
    			  // requestContext.getPageFlowScope().put(SEARCH_PARAMETERS,
    			  // searchObject);
    			  // }
    			  setCurrentState(STATE_EDIT);
    			  setListaAlterar();
//    		  }
//    		  else
//    		  {
//    			  // N�o altera estado, n�o selecionou ningu�m
//    			  FacesContext.getCurrentInstance().addMessage(
//    					  null,
//    					  new FacesMessage(FacesMessage.SEVERITY_WARN,
//    							  noItemSelectedMessage, null));
//    		  }
    	  }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }

   }

   /**
    * Retorna a entidade da linha selecionada na tabela
    */
   @SuppressWarnings("unchecked")
   protected Entity getSelectedRowEntity()
   {
      return (Entity) tblData.getRowData(); // getSelectedRowData();
   }

   /**
    * M�todo que deve ser sobreescrito para preencher as lista que ser�o
    * utilizadas como dados no preenchimento dos campos que ser�o inseridos
    * 
    * @param event - evento que foi disparado na tela
    */
   public void preparaInserir(ActionEvent event)
   {
      try
      {
    	 if(validarAcesso(Variaveis.ACAO_PREPARA_INSERIR))
    	 {
    		 setEntity(getNewEntityInstance());
    		 setCurrentState(STATE_INSERT);
    		 
    		 setDefaultInserir();
    		 setListaInserir();
    	 }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   protected void setListaInserir() throws Exception
   {
   }
   
   protected void setDefaultInserir() throws Exception
   {
   }
   
   protected void setListaAlterar() throws Exception
   {
      setListaInserir();
   }

   /**
    * Seleciona um determinado registro da tabela de resultados da consulta para
    * ser alterado ou apenas visualizado.
    * 
    * @param event - evento que foi disparado na tela
    */
   public void exibirSelecionado(ActionEvent event)
   {
      try
      {    	
		 if (this.getSelectedRowEntity() != null)
		 {
			 setEntity(getSelectedRowEntity());
			 
			 // Alterna os panels necess�rios para mostrar o conte�do da
			 // visualiza��o
			 setCurrentState(STATE_VIEW);
			 
			 setListaExibirSelecionado();
		 }
		 else
		 {
			 FacesContext.getCurrentInstance().addMessage(
					 null,
					 new FacesMessage(FacesMessage.SEVERITY_WARN,
							 noItemSelectedMessage, null));
		 }    	 
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   protected void setListaExibirSelecionado() throws Exception
   {
   }

   /**
    * Seleciona um determinado registro da tabela de resultados da consulta para
    * ser exclu�do
    * 
    * @param event - evento que foi disparado na tela
    */
   public void preparaExcluir(ActionEvent event)
   {
	  if(validarAcesso(Variaveis.ACAO_PREPARA_EXCLUIR))
	  {
		  if (this.getSelectedRowEntity() != null)
		  {
			  setEntity(getSelectedRowEntity());
			  
			  // Alterna os panels necess�rios para mostrar o conte�do da exclus�o
			  setCurrentState(STATE_DELETE);
		  }
		  else
		  {
			  FacesContext.getCurrentInstance().addMessage(
					  null,
					  new FacesMessage(FacesMessage.SEVERITY_WARN,
							  noItemSelectedMessage, null));
		  }
	  }
   }
   
   public void preparaVisualizar()
   {
	   try
	   {
		   setCurrentState(STATE_VIEW);
		   setListaAlterar();
	   }
	   catch (Exception e)
	   {
		   FacesMessage message = new FacesMessage(e.getMessage());
		   message.setSeverity(FacesMessage.SEVERITY_ERROR);
		   if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   /**
    * Exluir um determinado registro do banco de dados
    * 
    * @param event - evento que foi disparado na tela
    */
   public void remover(ActionEvent event)
   {
      try
      {
    	  if(validarAcesso(Variaveis.ACAO_REMOVER))
    	  {
    		  if (this.getSelectedRowEntity() != null)
    		  {
    			  //business.excluir(getSelectedRowEntity());
    			  
    			  FacesContext.getCurrentInstance().addMessage(
    					  null,
    					  new FacesMessage(FacesMessage.SEVERITY_INFO,
    							  "Item removido com sucesso.", null));
    			  
    			  pesquisar(event);
    			  
    			  setCurrentState(STATE_SEARCH);
    		  }
    	  }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   /**
    * Exluir um determinado registro do banco de dados
    * 
    * @param event - evento que foi disparado na tela
    */
   public void excluir(ActionEvent event)
   {
      try
      {
    	 if(validarAcesso(Variaveis.ACAO_EXCLUIR))
    	 {
    		 //business.excluir(getEntity());
    		 
    		 FacesContext.getCurrentInstance().addMessage(
    				 null,
    				 new FacesMessage(FacesMessage.SEVERITY_INFO,
    						 "Item exclu�do com sucesso.", null));
    		 
    		 pesquisar(event);
    		 
    		 setCurrentState(STATE_SEARCH);
    	 }
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   /**
    * Cancela a inclus�o / altera��o.
    * 
    * @param event - evento que foi disparado na tela
    */
   public void cancelar(ActionEvent event)
   {
      try
      {
    	 //if(validarAcesso(Variaveis.ACAO_CANCELAR))
    	 //{
    		 // Cancelar as opera��es, volta para a consulta
    		 setCurrentState(STATE_SEARCH);
    		 // limpa dos dados dos componentes de edi��o
    		 cleanSubmittedValues(pnlEditing);
    		 // remove a entidade selecionada colocando uma nova, vazia no lugar
    		 //setSearchObject(getNewEntityInstance());
    		 // realiza a pesquisa para preenchimento dos combos
    		 //setListaPesquisa();
    		 // seto a lista que foi pesquisada para null
    		 //setSearchResult(null);
    	 //}
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }
   
   /**
    * Cancela a inclus�o / altera��o.
    * 
    * @param event - evento que foi disparado na tela
    */
   public void clean(ActionEvent event)
   {
      try
      {
		 setCurrentState(STATE_SEARCH);		 
		 cleanSubmittedValues(pnlEditing);		
		 setSearchObject(getNewEntityInstance());		 
		 setListaPesquisa();
		 setSearchResult(null);
      }
      catch (Exception e)
      {
         FacesMessage message = new FacesMessage(e.getMessage());
         message.setSeverity(FacesMessage.SEVERITY_ERROR);
         if(e.getMessage() == null)
        	 FacesContext.getCurrentInstance().addMessage("", message);
         else
        	 FacesContext.getCurrentInstance().addMessage(null, message);
      }
   }

   /**
    * Limpa filtro de pesquisa
    */
   public void cleanSearchObject(ActionEvent event)
   {
      this.setSearchObject(getNewEntityInstance());
      this.cleanSubmittedValues(event.getComponent().getParent());
   }

   /**
    * Remove os dados armazenados na fase Apply Request Values para o componente
    * de id igual a <code>idComp</code>.
    * 
    * @param nomeComp
    */
   protected void cleanSubmittedValues(String idComp)
   {
      UIComponent component = FacesContext.getCurrentInstance().getViewRoot()
            .findComponent(idComp);
      cleanSubmittedValues(component);
   }

   /**
    * Limpa os dados dos componentes de edi��o e de seus filhos, recursivamente.
    * Checa se o componente � inst�ncia de EditableValueHolder e 'reseta' suas
    * propriedades.
    * 
    * @param component
    */
   protected void cleanSubmittedValues(UIComponent component)
   {
      if (component != null)
      {
         if (component instanceof EditableValueHolder)
         {
            EditableValueHolder evh = (EditableValueHolder) component;
            evh.setSubmittedValue(null);
            evh.setLocalValueSet(false);
            evh.setValid(true);
         }
         if (component.getChildCount() > 0)
         {
            for (UIComponent child : component.getChildren())
            {
               cleanSubmittedValues(child);
            }
         }
      }
   }

   /**
    * Retorna o resultado da consulta armazenada no escopo de fluxo de p�gina.
    * PageSlowScope. Nesse caso, n�o h� um atributo para manter o resultado
    * porque � necess�rio um scope maior que o "request" e menor que o
    * "session".
    * 
    * @return
    */
   public List<Entity> getSearchResult()
   {
      return this.searchResult;

   }

   /**
    * Atribui o resultado para o pageFlowScope.
    * 
    * @param resultadoConsulta
    */
   public void setSearchResult(List<Entity> searchResult)
   {
      this.searchResult = searchResult;
   }

   /**
    * Retorna o estado do fluxo do cadastro.
    * 
    * @return
    */
   public String getCurrentState()
   {
      return this.currentState;
   }

   /**
    * Atribui o estado do fluxo do cadastro, apenas acessado internamente.
    * 
    * @param state
    */
   public void setCurrentState(String state)
   {
      this.currentState = state;
   }

   /**
    * Retorna um componente que possua o id igual a <code>compId</code>.
    * 
    * @param compId Id do componente no ViewRoot
    * @return um componente que possua o id igual a <code>compId</code>.
    */
   protected UIComponent findComponent(String compId)
   {
      return FacesContext.getCurrentInstance().getViewRoot().findComponent(
            compId);
   }

   /**
    * Retorna o componente que exibe os dados
    * 
    * @return - componente que exibe os dados
    */
   public UIDataTable getTblData()
   {
      return tblData;
   }

   /**
    * Altera o componente que exibe os dados
    * 
    * @param tblData - componente que exibe os dados
    */
   public void setTblData(UIDataTable tblData)
   {
      this.tblData = tblData;
   }

   /**
    * Retorna a chave no arquivo de recurso para a mensagem exibida ao usu�rio
    * quando ele n�o escolher nenhum item.
    * 
    * @return - a chave no arquivo de recurso para a mensagem exibida ao usu�rio
    *         quando ele n�o escolher nenhum item.
    */
   public String getNoItemSelectedMessage()
   {
      return noItemSelectedMessage;
   }

   /**
    * Altera a chave no arquivo de recurso para a mensagem exibida ao usu�rio
    * quando ele n�o escolher nenhum item.
    * 
    * @param noItemSelectedMessage - a chave no arquivo de recurso para a
    *           mensagem exibida ao usu�rio quando ele n�o escolher nenhum item.
    */
   public void setNoItemSelectedMessage(String noItemSelectedMessage)
   {
      this.noItemSelectedMessage = noItemSelectedMessage;
   }

   /**
    * Retorna a chave do arquivo de recursor para a mensagem exibida ao usu�rio
    * quando a busca n�o retornar nenhum resultado.
    * 
    * @return - a chave do arquivo de recursor para a mensagem exibida ao
    *         usu�rio quando a busca n�o retornar nenhum resultado.
    */
   public String getNoResultMessage()
   {
      return noResultMessage;
   }

   /**
    * Altera a chave do arquivo de recursor para a mensagem exibida ao usu�rio
    * quando a busca n�o retornar nenhum resultado.
    * 
    * @param noResultMessage - a chave do arquivo de recursor para a mensagem
    *           exibida ao usu�rio quando a busca n�o retornar nenhum resultado.
    */
   public void setNoResultMessage(String noResultMessage)
   {
      this.noResultMessage = noResultMessage;
   }

   /**
    * Retorna a entidade usada para as opera��es de editar, excluir, inserir e
    * pesquisar.
    * 
    * @return - a entidade usada para as opera��es de editar, excluir, inserir e
    *         pesquisar.
    */
   public Entity getEntity()
   {
      return this.entity;
   }

   /**
    * Altera a entidade usada para as opera��es de editar, excluir, inserir e
    * pesquisar.
    * 
    * @param entity - a entidade usada para as opera��es de editar, excluir,
    *           inserir e pesquisar.
    */
   public void setEntity(Entity entity)
   {
      if (entity == null) entity = this.getNewEntityInstance();
      this.entity = entity;
   }

   /**
    * Retorna o objeto que armazena os par�metros da busca.
    * 
    * @return - o objeto que armazena os par�metros da busca.
    */
   public Entity getSearchObject()
   {
      return this.searchObject;
   }

   /**
    * Altera o objeto que armazena os par�metros da busca.
    * 
    * @param searchObject - o objeto que armazena os par�metros da busca.
    */
   public void setSearchObject(Entity searchObject)
   {
      if (searchObject == null) searchObject = this.getNewEntityInstance();
      this.searchObject = searchObject;
   }

   /**
    * Retorna o n�gocio utilizado para persistir e extrair as informa��es do
    * banco de dados
    * 
    * @return - o n�gocio utilizado para persistir e extrair as informa��es do
    *         banco de dados
    */
   public Business getBusiness()
   {
      return business;
   }

   /**
    * Altera o n�gocio utilizado para persistir e extrair as informa��es do
    * banco de dados
    * 
    * @param business - o n�gocio utilizado para persistir e extrair as
    *           informa��es do banco de dados
    */
   public void setBusiness(Business business)
   {
      this.business = business;
   }

   /**
    * Reetorna o painel de edi��o
    * 
    * @return - o painel de edi��o
    */
   public UIComponent getPnlEditing()
   {
      return pnlEditing;
   }

   /**
    * Altera o painel de edi��o
    * 
    * @param pnlEditing - o painel de edi��o
    */
   public void setPnlEditing(UIComponent pnlEditing)
   {
      this.pnlEditing = pnlEditing;
   }

   /**
    * Retorna <code>true</code> se o estado estiver em editing.
    * 
    * @return - <code>true</code> se o estado estiver em editing.
    */
   public boolean isEditing()
   {
      return STATE_EDIT.equals(getCurrentState());
   }

   /**
    * Retorna <code>true</code> se o estado estiver em inser��o.
    * 
    * @return - <code>true</code> se o estado estiver em inser��o.
    */
   public boolean isInserting()
   {
      return STATE_INSERT.equals(getCurrentState());
   }

   /**
    * Retorna <code>true</code> se o estado estiver em exclus�o.
    * 
    * @return - <code>true</code> se o estado estiver em exclus�o.
    */
   public boolean isDeleting()
   {
      return STATE_DELETE.equals(getCurrentState());
   }

   /**
    * Retorna <code>true</code> se o estado estiver em visualiza��o.
    * 
    * @return - <code>true</code> se o estado estiver em visualiza��o.
    */
   public boolean isViewing()
   {
      return STATE_VIEW.equals(getCurrentState());
   }

   /**
    * Retorna <code>true</code> se o estado estiver em atualiza��o, ou seja,
    * caso o estado esteja em edi��o ou inser��o ou exclus�o.
    * 
    * @return - <code>true</code> se o estado estiver em atualiza��o, ou seja,
    *         caso o estado esteja em edi��o ou inser��o ou exclus�o.
    */
   public boolean isUpdating()
   {
      return (this.isEditing() || this.isInserting() || this.isDeleting());
   }

   /**
    * Retorna <code>true</code> se o estado estiver em busca.
    * 
    * @return - <code>true</code> se o estado estiver em busca.
    */
   public boolean isSearching()
   {
      return (getCurrentState() == null || getCurrentState().equals("") || STATE_SEARCH
            .equals(getCurrentState()));
   }

   /**
    * Retorna o t�tulo da p�gina atual
    * 
    * @return - o t�tulo da p�gina atual
    */
   public String getTitleFromThePage()
   {
      final String currentState = this.getCurrentState();// == null ?
                                                         // STATE_SEARCH :
                                                         // this.getCurrentState();
      return titles.get(currentState);
   }

   /**
    * Retorna o t�tulo da p�gina concatenado com a descri��o que indica qual o
    * estado atual da p�gina
    * 
    * @return - o t�tulo da p�gina concatenado com a descri��o que indica qual o
    *         estado atual da p�gina
    */
   public String getFullTitle()
   {
      if ("Nenhum t�tulo definido.".equals(this.pageTitle))
      {
         return this.pageTitle;
      }
      else
      {
         String title = "";

         title += this.pageTitle;
         title += " - ";
         title += this.getTitleFromThePage();

         return title;
      }
   }

   /**
    * Retorna uma lista de crit�rios de ordena��o utilizados como default na
    * grid de resultado de pesquisa. Por default n�o h� nenhum crit�rio de
    * ordena��o, sendo, o m�todo retorna <code>null</code>.
    */
   // protected List<SortCriterion> getDefaultSortCriteria() {
   // return null;
   // }
   /**
    * Retorna uma chave �nica para os par�metros deste bean armazenados no
    * PageFlowScope
    */
   protected String getUniqueVariableName(String key)
   {
      String var = this.getClass().getSimpleName() + key;
      return var;
   }

   /**
    * Retorna a chave do arquivo de recurso que define o t�tulo da p�gina
    * 
    * @return - a chave do arquivo de recurso que define o t�tulo da p�gina
    */
   public String getPageTitle()
   {
      return pageTitle;
   }

   /**
    * Altera a chave do arquivo de recurso que define o t�tulo da p�gina
    * 
    * @param pageTitle - a chave do arquivo de recurso que define o t�tulo da
    *           p�gina
    */
   public void setPageTitle(String pageTitle)
   {
      this.pageTitle = pageTitle;
   }

   
   @SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
   public String imprimir() 
   {
	   try 
	   {
		   HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		   Utilitarios.removerArquivos(request.getRealPath("") + "/temp");
			
			if (getListaReport() == null 
					|| getListaReport().size() <= 0) 
			{
				FacesMessage message = new FacesMessage("Não existem dados para a impressão do relatório!");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				return null;
			}
			
			String pathJasper = request.getRealPath("WEB-INF/relatorios");
			
			String os = System.getProperty("os.name");
			File pasta = new File((os.toLowerCase().indexOf("linux") > -1 ? "/" : "\\"));
			pathJasper += pasta;
			
			Map parameters = new HashMap();
			parameters.put("SUBREPORT_DIR", pathJasper);
			
			this.validaRelatorio();
			this.configuraRelatorio(parameters, request);			
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(pathJasper + "/" + this.REPORT_NAME + ".jasper");
			
			if (jasperReport == null) 
			{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum relatório selecionado.", null));
			}
			
			String pathPdf = "/temp/" + request.getRequestedSessionId() + String.valueOf(Math.random() * 10000) + this.REPORT_NAME + ".pdf";
			
						
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(getSearchResult());			
			
			JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, ds);
	 
			JasperExportManager.exportReportToPdfFile(print, request.getRealPath("") + pathPdf);
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.sendRedirect(request.getContextPath() + pathPdf);
		}
		catch (Exception e) 
		{
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}
   
   	@SuppressWarnings("rawtypes")
	public List getListaReport()
   	{
   		return getSearchResult();
   	}
   	
   	/**
   	 * Setar a variável => REPORT_NAME;
   	 * Setar parametros
   	 */
   	@SuppressWarnings("rawtypes")
	public void configuraRelatorio(Map parameters, HttpServletRequest request){}
   	
   	/**
   	 * Realizar validacoes para o relatorio
   	 */
   	public void validaRelatorio(){}
   
//   /**
//    * Retorna <code>true</code> se o estado estiver em relat�rio.
//    * 
//    * @return - <code>true</code> se o estado estiver em relat�rio.
//    */
//   public boolean isReporting()
//   {
//      return (this.REPORT_NAME != null);
//   }
//
//   /**
//    * Retorna os par�metros que ser�o utilizados no relat�rio
//    * 
//    * @return - os par�metros que ser�o utilizados no relat�rio
//    */
//   @SuppressWarnings("rawtypes")
//   protected Map getParametersReport() throws Exception
//   {
//      return new HashMap();
//   }
//   
//   @SuppressWarnings("rawtypes")
//   protected Map getParametersReport(List<Entity> lista) throws Exception
//   {
//	   return getParametersReport();
//   }
//
//   /**
//    * M�todo que valida se todos os campos obrigat�rios do relat�rio foram
//    * preenchidos
//    * 
//    * @throws Exception - erro inesperado da aplica��o
//    */
//   protected void validateReport() throws Exception
//   {
//   }
   
   protected boolean validarAcesso(String acao) 
   {
	   return true;
   }

}
