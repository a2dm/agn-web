//ScrollUp
$(function () {
	$.scrollUp({
  		scrollName: 'scrollUp', // Element ID
  		topDistance: '300', // Distance from top before showing element (px)
  		topSpeed: 300, // Speed back to top (ms)
  		animation: 'fade', // Fade, slide, none
      	animationInSpeed: 400, // Animation in speed (ms)
      	animationOutSpeed: 400, // Animation out speed (ms)
      	scrollText: 'Top', // Text for element
      	activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
	});
});

//Tooltip
$('a').tooltip('hide');

//Popover
$('.popover-pop').popover('hide');

//Dropdown
$('.dropdown-toggle').dropdown();

  $(document).ready(function() {
  
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    
    var data = y + '-' + m + '-' + d;
    
    var calendar = $('#calendar').fullCalendar({
	  header: {
	    left: 'prev,next today',
	    center: 'title',
	    right: 'month,agendaWeek,agendaDay'
	  },
	  
	  selectable: true,
	  
	  selectHelper: false,
  
	  select: function(start, end, allDay) {
		  abrirModalAgendamento(start,end,allDay);
	  },  
  
	  editable: false,
	  
	  events: "/agn-web/CalendarServlet?idCliPro="+document.getElementById('formulario:idClinicaProfissional').value+"&dtAgn="+document.getElementById('formulario:dataCalendario').value ,
	  
	  eventColor: '#ffbf00',
	  
	  eventRender: function(event, element) {
	        if (event.className == 'agendada') {
	            element.css({
	                'background-color': '#3187bf',
	                'border-color': '#333333'
	            });
	        }
	        
	        if (event.className == 'presente') {
	            element.css({
	                'background-color': '#bf00ff',
	                'border-color': '#333333'	                	
	            });
	        }
	        
	        if (event.className == 'atendimento') {
	            element.css({
	                'background-color': '#ffbf00',
	                'border-color': '#333333'
	            });
	        }
	        
	        if (event.className == 'concluida') {
	            element.css({
	                'background-color': '#378006',
	                'border-color': '#333333'
	            });
	        }
	    },
	    
	  viewRender: function(start, view,element) {
		  document.getElementById('formulario:dataCalendario').value = start;
	    },
	  
	  eventClick: 
		  function(event, jsEvent, view) 
		  {
		  	alert(event.id);
		  	abrirModalAlterarAgendamento(event.id);
		  }	  
	    });
	  });
  